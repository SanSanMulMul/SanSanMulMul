package com.sansantek.sansanmulmul.ui.view.register

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentRegisterProfileBinding
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.view.LoginActivity
import com.sansantek.sansanmulmul.ui.viewmodel.LoginActivityViewModel

private const val TAG = "RegisterProfileFragment 싸피"

class RegisterProfileFragment : BaseFragment<FragmentRegisterProfileBinding>(
    FragmentRegisterProfileBinding::bind,
    R.layout.fragment_register_profile
) {
    private val activityViewModel: LoginActivityViewModel by activityViewModels()
    private lateinit var activity: LoginActivity
    private val permissionList = arrayOf(Manifest.permission.CAMERA)
    private lateinit var permissionChecker: PermissionChecker
    private lateinit var uri: Uri
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionChecker = PermissionChecker(this)
        binding.ibEditPrifileImg.setOnClickListener {
            if (permissionChecker.checkPermission(activity, permissionList)) {
                permissionChecker.setOnGrantedListener { //퍼미션 획득 성공일때
                    openGallery()
                }
                permissionChecker.requestPermissionLauncher.launch(permissionList) // 권한없으면 창 띄움
            } else {
                openGallery()
            }
        }

    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK)
        gallery.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        pickImageLauncher.launch(gallery)
        Log.d(TAG, "openGallery: 사진 추가")
    }

    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openGallery()
            } else
                Log.d(TAG, "deny")
        }
    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { it ->
                    Log.d(TAG, "수신 양호: $it")

                    activityViewModel.setUri(it)
                    binding.ivProfile.setImageURI(it)
                }
            }
        }
}