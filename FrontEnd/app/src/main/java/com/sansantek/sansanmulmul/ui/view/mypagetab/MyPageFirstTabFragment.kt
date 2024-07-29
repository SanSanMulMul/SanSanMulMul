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

class MyPageFirstTabFragment : BaseFragment<FragmentMyPageTabBinding>(
    FragmentMyPageTabBinding::bind,
    R.layout.fragment_my_page_tab
) {
    private lateinit var myPageHikingStyleListAdapter: MyPageHikingStyleListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}