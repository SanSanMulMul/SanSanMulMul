package com.sansantek.sansanmulmul.ui.view.register

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentRegisterProfileBinding
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.view.LoginActivity

private const val TAG = "RegisterProfileFragment 싸피"
class RegisterProfileFragment : BaseFragment<FragmentRegisterProfileBinding>(
    FragmentRegisterProfileBinding::bind,
    R.layout.fragment_register_profile
) {
    private lateinit var activity: LoginActivity
    private val permissionList = arrayOf(Manifest.permission.CAMERA)
    private lateinit var permissionChecker : PermissionChecker
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as LoginActivity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionChecker = PermissionChecker(this)
        binding.ibEditPrifileImg.setOnClickListener {
            if(permissionChecker.checkPermission(activity, permissionList)){
                permissionChecker.setOnGrantedListener { //퍼미션 획득 성공일때
                    openGallery()
                }
                permissionChecker.requestPermissionLauncher.launch(permissionList) // 권한없으면 창 띄움
            }
            else{
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

                    val image = it
                    binding.ivProfile.setImageURI(image)
                }
            }
        }
}