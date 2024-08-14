package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.MountainHistory
import com.sansantek.sansanmulmul.databinding.FragmentMyPageFirstTabBinding
import com.sansantek.sansanmulmul.ui.adapter.MyPageFirstTabFavoriteMountainListAdapter
import com.sansantek.sansanmulmul.ui.adapter.MyPageFirstTabHistoryMountainListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.SpaceItemDecoration
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch
import java.util.Date

class MyPageFirstTabFragment : BaseFragment<FragmentMyPageFirstTabBinding>(
    FragmentMyPageFirstTabBinding::bind,
    R.layout.fragment_my_page_first_tab
) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private var historyMountainList = mutableListOf(
        MountainHistory(R.drawable.mountain_gum_o, "금오산", Date()),
//        MountainHistory(R.drawable.dummy2, "가리산", Date()),
//        MountainHistory(R.drawable.dummy3, "가리왕sssssszssss산", Date()),
//        MountainHistory(R.drawable.dummy3, "가리왕산", Date()),
//        MountainHistory(R.drawable.dummy3, "가리왕산", Date()),
//        MountainHistory(R.drawable.dummy3, "가리왕산", Date()),
//        MountainHistory(R.drawable.dummy3, "가리왕산", Date())
    )

    private lateinit var favoriteMountainAdapter: MyPageFirstTabFavoriteMountainListAdapter
    private lateinit var historyMountainAdapter: MyPageFirstTabHistoryMountainListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavoriteMountain.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).apply {
                    isMeasurementCacheEnabled = false
                }
            favoriteMountainAdapter = MyPageFirstTabFavoriteMountainListAdapter()
            loadMyFavoriteMountainList()
            adapter = favoriteMountainAdapter
            addItemDecoration(SpaceItemDecoration(30))
        }
        binding.rvMountainHistory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).apply {
                    isMeasurementCacheEnabled = false
                }
            historyMountainAdapter = MyPageFirstTabHistoryMountainListAdapter()
            historyMountainAdapter.apply {
                submitList(historyMountainList)
                setItemClickListener(object :
                    MyPageFirstTabHistoryMountainListAdapter.ItemClickListener {
                    override fun onHistoryClick(position: Int) {
                        ShowMyPageHistoryDialog().show(childFragmentManager, null)
                    }

                })
            }
            adapter = historyMountainAdapter
            addItemDecoration(SpaceItemDecoration(30))
        }
    }

    private fun loadMyFavoriteMountainList() {
        activityViewModel.token?.let {
            lifecycleScope.launch {
                val result =
                    mountainService.getLikedMountainList(makeHeaderByAccessToken(it.accessToken))
                if (result.isSuccessful) {
                    favoriteMountainAdapter.submitList(result.body()!!)
                }
            }
        }
    }

    private fun loadMyHikingHistory() {
        activityViewModel.token?.let {
            lifecycleScope.launch {
                val result =
                    mountainService.getLikedMountainList(makeHeaderByAccessToken(it.accessToken))
                if (result.isSuccessful) {
                    favoriteMountainAdapter.submitList(result.body()!!)
                }
            }
        }
    }
}