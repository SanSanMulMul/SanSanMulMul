package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Picture
import com.sansantek.sansanmulmul.data.model.Weather
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabSecondHikingInfoFragmentBinding
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabThirdGalleryDetailFragmentBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailTabGalleryInfoListAdapter
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailTabWeatherInfoListAdapter
import com.sansantek.sansanmulmul.ui.adapter.GroupGalleryViewPagerAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.DiviiderItemDecorator
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "GroupDetailTabThirdGalleryDetailFragment_싸피"
class GroupDetailTabThirdGalleryDetailFragment(val position: Int) : BaseFragment<FragmentGroupDetailTabThirdGalleryDetailFragmentBinding>(
    FragmentGroupDetailTabThirdGalleryDetailFragmentBinding::bind,
    R.layout.fragment_group_detail_tab_third_gallery_detail_fragment
) {
    private lateinit var pictureAdapter: GroupGalleryViewPagerAdapter
    private val pictureList = mutableListOf(Picture("박태우스"), Picture("윤가희"), Picture("노나현"), Picture("정민선"), Picture("곽대건"), Picture("신영민"))

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val parentFragment = parentFragment as GroupDetailFragment
                    parentFragment.popBackStackGroupDetailFragmentView()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpPicture.apply {
            pictureAdapter = GroupGalleryViewPagerAdapter()
            adapter = pictureAdapter.apply {
                pictureAdapter.submitList(pictureList)
            }
            setCurrentItem(position, false)
        }
    }
}