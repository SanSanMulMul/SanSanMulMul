package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.os.Bundle
import android.view.View
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMyPageSecondTabBinding
import com.sansantek.sansanmulmul.ui.adapter.MyPageFirstTabFavoriteMountainListAdapter
import com.sansantek.sansanmulmul.ui.adapter.MyPageFirstTabHistoryMountainListAdapter

class GroupMemberDetailSecondFragment : BaseFragment<FragmentMyPageSecondTabBinding>(
    FragmentMyPageSecondTabBinding::bind,
    R.layout.fragment_my_page_second_tab
) {


    private lateinit var favoriteMountainAdapter : MyPageFirstTabFavoriteMountainListAdapter
    private lateinit var historyMountainAdapter : MyPageFirstTabHistoryMountainListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}