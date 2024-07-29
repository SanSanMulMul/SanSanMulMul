package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMyPageTabBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.MyPageHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.layoutmanager.CustomLayoutmanager

class MyPageTabFragment : BaseFragment<FragmentMyPageTabBinding>(
    FragmentMyPageTabBinding::bind,
    R.layout.fragment_my_page_tab
) {
    private val styleList = mutableListOf("#등산도 식후경", "#등산은 사진이지", "#설렁설렁", "#저쩌구")
    private lateinit var myPageHikingStyleListAdapter: MyPageHikingStyleListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myPageHikingStyleListAdapter = MyPageHikingStyleListAdapter()
        binding.rvMyHikingStyle.apply {
            adapter = myPageHikingStyleListAdapter.apply {
                submitList(styleList)
            }
            layoutManager = CustomLayoutmanager(requireContext(), 2)
        }

        binding.tlTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {
                        childFragmentManager.beginTransaction().replace(binding.myPageFragmentView.id, MyPageFirstTabFragment()).commit()
                    }
                    else -> {
                        childFragmentManager.beginTransaction().replace(binding.myPageFragmentView.id, MyPageFirstTabFragment()).commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        super.onViewCreated(view, savedInstanceState)
    }
}