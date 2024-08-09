package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.CrewGallery
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabThirdGalleryInfoFragmentBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailTabGalleryInfoListAdapter
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.crewService
import com.sansantek.sansanmulmul.ui.util.Util.getRealPathFromURI
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.viewmodel.GroupDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


private const val TAG = "GroupDetailTabThirdGalleryInfoFragment_싸피"

class GroupDetailTabThirdGalleryInfoFragment(private val crew: Crew) :
    BaseFragment<FragmentGroupDetailTabThirdGalleryInfoFragmentBinding>(
        FragmentGroupDetailTabThirdGalleryInfoFragmentBinding::bind,
        R.layout.fragment_group_detail_tab_third_gallery_info_fragment
    ) {
    private lateinit var galleryAdapter: GroupDetailTabGalleryInfoListAdapter
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: GroupDetailViewModel by activityViewModels()
    private val REQ_STORAGE_PERMISSION = 0
    private var imageUri: Uri? = null
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
                Log.d(TAG, "data가 널인거야?? $data: ")
                data?.clipData?.let { it ->
                    Log.d(TAG, "여기까지는 오나?? 사진 선택 이후: ")
                    for(i in 0 until it.itemCount){
                        getRealPathFromURI(requireContext(), it.getItemAt(i).uri)?.let { path ->
                            val file = File(path)
                            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                            val body =
                                MultipartBody.Part.createFormData("image", file.name, requestBody)
                            activityViewModel.token?.let {
                                lifecycleScope.launch {
                                    val result = crewService.postPictureToGallery(
                                        makeHeaderByAccessToken(it.accessToken),
                                        crew.crewId,
                                        body
                                    )
                                    if (result.isSuccessful) {
                                        showToast("사진 추가 성공!")
                                        val picture = crewService.getCrewGalleryList(
                                            makeHeaderByAccessToken(it.accessToken),
                                            crew.crewId
                                        )
                                        if (picture.isSuccessful) {
                                            viewModel.setPictureList(picture.body()!!)
                                        }
                                    } else {
                                        showToast("사진 추가 실패ㅠㅠ")
                                        Log.d(TAG, "사진 추가 오류: ${result.code()}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryAdapter = GroupDetailTabGalleryInfoListAdapter()
        registerObserver()
        lifecycleScope.launch {
            activityViewModel.token?.let {
                val picture = crewService.getCrewGalleryList(
                    makeHeaderByAccessToken(it.accessToken),
                    crew.crewId
                )
                if (picture.isSuccessful) {
                    viewModel.setPictureList(picture.body()!!)
                }
            }
        }

        binding.rvGalleryList.apply {
            adapter = galleryAdapter.apply {
                submitList(viewModel.pictureList.value)
                layoutManager = GridLayoutManager(requireContext(), 3)
                setItemClickListener(object :
                    GroupDetailTabGalleryInfoListAdapter.ItemClickListener {
                    override fun onClick(position: Int) {
                        val groupDetailFragment = parentFragment as GroupDetailFragment
                        groupDetailFragment.changeAddToBackStackGroupDetailFragmentView(
                            GroupDetailTabThirdGalleryDetailFragment(position)
                        )
                    }
                })
            }
        }
        binding.fdPictureAddBtn.setOnClickListener {
            val readPermission = ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            )

            if (readPermission == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "onViewCreated: 권한 없음")

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQ_STORAGE_PERMISSION
                )
            } else {
                galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
    }

    private fun registerObserver(){
        viewModel.pictureList.observe(viewLifecycleOwner){
            galleryAdapter.submitList(it)
        }
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK)
        gallery.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        ).putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        pickImageLauncher.launch(gallery)
        Log.d(TAG, "openGallery: 사진 추가")
    }

}