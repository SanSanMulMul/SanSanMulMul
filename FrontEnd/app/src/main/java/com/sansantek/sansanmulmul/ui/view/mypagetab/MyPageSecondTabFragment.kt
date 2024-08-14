package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.MountainPeakStone
import com.sansantek.sansanmulmul.databinding.FragmentMyPageSecondTabBinding
import com.sansantek.sansanmulmul.ui.adapter.MountainPeakStoneListAdapter
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainPeakStoneViewModel

private const val TAG = "싸피_MyPageSecondTabFragment"

class MyPageSecondTabFragment : BaseFragment<FragmentMyPageSecondTabBinding>(
    FragmentMyPageSecondTabBinding::bind,
    R.layout.fragment_my_page_second_tab
) {
    private var stoneList: MutableList<MountainPeakStone> = mutableListOf()
    private lateinit var mountainPeakStoneListAdapter: MountainPeakStoneListAdapter
    private val mountainPeakStoneViewModel: MountainPeakStoneViewModel by activityViewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initStoneIdObserving()
        initRecyclerView()
        initData()
    }

    private fun initData() {
        mountainPeakStoneViewModel.fetchAllMountainPeakStones()
        mountainPeakStoneViewModel.fetchMyAllMountainPeakStones(activityViewModel.user.userId)
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

    private fun initStoneIdObserving() {

        // 전체 정상석
        mountainPeakStoneViewModel.mountainPeakStoneList.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                stoneList = list.toMutableList()
            }
            mountainPeakStoneListAdapter.submitList(list)
        }

        // 내 인증된 정상석 조회 시
        mountainPeakStoneViewModel.myMountainPeakStoneList.observe(viewLifecycleOwner) { myMountainPeakStoneList ->
            Log.d(TAG, "initStoneIdObserving: mystoneList = $myMountainPeakStoneList")
            myMountainPeakStoneList?.let {
                mountainPeakStoneListAdapter.setStoneList(it.toMutableList())
            }

            myMountainPeakStoneList?.forEach { myMountainStone ->

            }

        }
    }


}