package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainHistory
import com.sansantek.sansanmulmul.databinding.FragmentMyPageFirstTabBinding
import com.sansantek.sansanmulmul.databinding.FragmentMyPageSecondTabBinding
import com.sansantek.sansanmulmul.ui.adapter.MyPageFirstTabFavoriteMountainListAdapter
import com.sansantek.sansanmulmul.ui.adapter.MyPageFirstTabHistoryMountainListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.HorizontalMarginItemDecoration
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.SpaceItemDecoration
import com.sansantek.sansanmulmul.ui.adapter.layoutmanager.CustomLayoutmanager
import java.util.Date

class MyPageSecondTabFragment : BaseFragment<FragmentMyPageSecondTabBinding>(
    FragmentMyPageSecondTabBinding::bind,
    R.layout.fragment_my_page_second_tab
) {


    private lateinit var favoriteMountainAdapter : MyPageFirstTabFavoriteMountainListAdapter
    private lateinit var historyMountainAdapter : MyPageFirstTabHistoryMountainListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}