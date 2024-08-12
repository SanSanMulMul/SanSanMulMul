package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.CrewGallery
import com.sansantek.sansanmulmul.data.model.Picture
import com.sansantek.sansanmulmul.data.model.Weather
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabSecondHikingInfoFragmentBinding
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabThirdGalleryDetailFragmentBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailTabGalleryInfoListAdapter
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailTabWeatherInfoListAdapter
import com.sansantek.sansanmulmul.ui.adapter.GroupGalleryViewPagerAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.DiviiderItemDecorator
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.crewService
import com.sansantek.sansanmulmul.ui.util.Util
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.viewmodel.GroupDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "GroupDetailTabThirdGalleryDetailFragment_싸피"
class GroupDetailTabThirdGalleryDetailFragment(val position: Int, val crew: Crew) : BaseFragment<FragmentGroupDetailTabThirdGalleryDetailFragmentBinding>(
    FragmentGroupDetailTabThirdGalleryDetailFragmentBinding::bind,
    R.layout.fragment_group_detail_tab_third_gallery_detail_fragment
) {
    private lateinit var pictureAdapter: GroupGalleryViewPagerAdapter
    private val viewModel: GroupDetailViewModel by activityViewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

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
                pictureAdapter.submitList(viewModel.pictureList.value)
                Log.d(TAG, "onViewCreated: $position 사진 리스트 볼까? ${viewModel.pictureList.value}")
            }
            setCurrentItem(position, false)
        }
        binding.tvDeleteBtn.setOnClickListener {
            activityViewModel.token?.let {
                lifecycleScope.launch {
                    val result = crewService.deleteGallery(makeHeaderByAccessToken(it.accessToken), crew.crewId, viewModel.pictureList.value!![viewModel.position.value!!].picId)
                    if(result.isSuccessful){
                        showToast("사진을 삭제했습니다!")
                        val groupDetailFragment = parentFragment as GroupDetailFragment
                        groupDetailFragment.popBackStackGroupDetailFragmentView()
                    }else{
                        showToast("삭제에 실패했습니다!")
                    }
                }
            }
        }
        binding.tvListBtn.setOnClickListener {
            val groupDetailFragment = parentFragment as GroupDetailFragment
            groupDetailFragment.popBackStackGroupDetailFragmentView()
        }
        registerObserve()
    }

    private fun registerObserve(){
        viewModel.pictureList.observe(viewLifecycleOwner){
            pictureAdapter.submitList(it)
        }
        viewModel.position.observe(viewLifecycleOwner){
            if(viewModel.pictureList.value!![it].owner) {
                binding.tvDeleteBtn.visibility = View.VISIBLE
            }else{
                binding.tvDeleteBtn.visibility = View.GONE
            }
        }
    }
}