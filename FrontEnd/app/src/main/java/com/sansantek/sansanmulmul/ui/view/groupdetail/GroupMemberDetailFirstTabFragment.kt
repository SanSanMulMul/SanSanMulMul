package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMyPageFirstTabBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupMemberDetailFirstTabFavoriteMountainListAdapter
import com.sansantek.sansanmulmul.ui.adapter.GroupMemberDetailFirstTabHistoryMountainListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.SpaceItemDecoration
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.recordService
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

private const val TAG = "GroupMemberDetailFirstT 싸피"
class GroupMemberDetailFirstTabFragment : BaseFragment<FragmentMyPageFirstTabBinding>(
    FragmentMyPageFirstTabBinding::bind,
    R.layout.fragment_my_page_first_tab
) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var favoriteMountainAdapter: GroupMemberDetailFirstTabFavoriteMountainListAdapter
    private lateinit var historyMountainAdapter: GroupMemberDetailFirstTabHistoryMountainListAdapter
    private var memberUserId: Int? = null

    companion object {
        fun newInstance(memberUserId: Int): GroupMemberDetailFirstTabFragment {
            val fragment = GroupMemberDetailFirstTabFragment()
            val args = Bundle().apply {
                putInt("followUserId", memberUserId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memberUserId = arguments?.getInt("followUserId")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 즐겨찾기 산 리스트 설정
        binding.rvFavoriteMountain.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).apply {
                    isMeasurementCacheEnabled = false
                }
            favoriteMountainAdapter = GroupMemberDetailFirstTabFavoriteMountainListAdapter()
            loadGroupMemberFavoriteMountain()
            adapter = favoriteMountainAdapter
            addItemDecoration(SpaceItemDecoration(30))
        }

        // 등산 히스토리 리스트 설정
        binding.rvMountainHistory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).apply {
                    isMeasurementCacheEnabled = false
                }
            historyMountainAdapter = GroupMemberDetailFirstTabHistoryMountainListAdapter()
            loadGroupMemberHikingHistory()
            adapter = historyMountainAdapter
            addItemDecoration(SpaceItemDecoration(30))
        }
    }

    private fun loadGroupMemberFavoriteMountain() {
        memberUserId?.let { id ->
            lifecycleScope.launch {
                val result = mountainService.getMemberLikeMountain(id)
                if (result.isSuccessful) {
                    favoriteMountainAdapter.submitList(result.body()!!)
                    Log.d(TAG, "즐겨찾기 데이터: ${result} ")
                }
            }
        }
    }

    private fun loadGroupMemberHikingHistory() {
        memberUserId?.let { id ->
            lifecycleScope.launch {
                val result = recordService.getAllMemberHikingRecord(id)
                if (result.isSuccessful) {
                    historyMountainAdapter.submitList(result.body()!!)
                }
            }
        }
    }
}
