package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.MountainPeakStoneItem
import com.sansantek.sansanmulmul.databinding.FragmentMyPageSecondTabBinding
import com.sansantek.sansanmulmul.ui.adapter.MountainPeakStoneListAdapter
import com.sansantek.sansanmulmul.ui.viewmodel.MountainPeakStoneViewModel

class MyPageSecondTabFragment : BaseFragment<FragmentMyPageSecondTabBinding>(
  FragmentMyPageSecondTabBinding::bind,
  R.layout.fragment_my_page_second_tab
) {
  private var stoneList: MutableList<MountainPeakStoneItem> = mutableListOf()
  private lateinit var mountainPeakStoneListAdapter: MountainPeakStoneListAdapter
  private val mountainPeakStoneViewModel: MountainPeakStoneViewModel by viewModels()


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    initRecyclerView()
    initData()

    
  }

  private fun initData() {
    mountainPeakStoneViewModel.fetchAllMountainPeakStones()

    mountainPeakStoneViewModel.mountainPeakStoneList.observe(viewLifecycleOwner) { list ->
      if (list != null) {
        stoneList = list
      }
      mountainPeakStoneListAdapter.submitList(list)
    }
  }

  private fun initRecyclerView() {
    val recyclerView = binding.rvMountainPeakStone
    mountainPeakStoneListAdapter = MountainPeakStoneListAdapter()
    val gridLayoutManager = GridLayoutManager(context, 4)
    recyclerView.layoutManager = gridLayoutManager

    recyclerView.apply {
      adapter = mountainPeakStoneListAdapter.apply {
        submitList(stoneList)
      }
    }
  }
}