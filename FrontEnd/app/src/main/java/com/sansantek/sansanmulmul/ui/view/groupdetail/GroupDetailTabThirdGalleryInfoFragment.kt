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
import androidx.recyclerview.widget.GridLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.Picture
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabThirdGalleryInfoFragmentBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailTabGalleryInfoListAdapter
import okhttp3.internal.format


private const val TAG = "GroupDetailTabThirdGalleryInfoFragment_싸피"
class GroupDetailTabThirdGalleryInfoFragment(private val crew: Crew) : BaseFragment<FragmentGroupDetailTabThirdGalleryInfoFragmentBinding>(
    FragmentGroupDetailTabThirdGalleryInfoFragmentBinding::bind,
    R.layout.fragment_group_detail_tab_third_gallery_info_fragment
) {
    private lateinit var galleryAdapter: GroupDetailTabGalleryInfoListAdapter
    private var pictureList = mutableListOf(Picture("박태우스"), Picture("윤가희"), Picture("노나현"), Picture("정민선"), Picture("곽대건"), Picture("신영민") )
    private val REQ_STORAGE_PERMISSION = 0
    private var imageUri: Uri? = null
    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                openGallery()
            }else
                Log.d(TAG, "deny")
        }
    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val newList = mutableListOf<Picture>()
                data?.clipData?.let { it ->
                    for (i in 0 until it.itemCount) {
                        newList.add(Picture("박태우스", it.getItemAt(i).uri))
                    }
                }
                Log.d(TAG, "imgUri: $imageUri")
                pictureList = (pictureList + newList).toMutableList()
                galleryAdapter.submitList(pictureList)
                Log.d(TAG, "갤러리 추가 결과: 갤러리에 데이터 도작")
                Log.d(TAG, ": $pictureList")
            }
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryAdapter = GroupDetailTabGalleryInfoListAdapter()
        binding.rvGalleryList.apply {
            adapter = galleryAdapter.apply {
                submitList(pictureList)
                layoutManager = GridLayoutManager(requireContext(), 3)
                setItemClickListener(object : GroupDetailTabGalleryInfoListAdapter.ItemClickListener{
                    override fun onClick(position: Int) {
                        val groupDetailFragment = parentFragment as GroupDetailFragment
                        groupDetailFragment.changeAddToBackStackGroupDetailFragmentView(GroupDetailTabThirdGalleryDetailFragment(position))
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

                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQ_STORAGE_PERMISSION)
            } else {
                galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
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