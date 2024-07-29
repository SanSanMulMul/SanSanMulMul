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
import com.sansantek.sansanmulmul.ui.adapter.MyPageFirstTabFavoriteMountainListAdapter
import com.sansantek.sansanmulmul.ui.adapter.MyPageFirstTabHistoryMountainListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.HorizontalMarginItemDecoration
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.SpaceItemDecoration
import com.sansantek.sansanmulmul.ui.adapter.layoutmanager.CustomLayoutmanager
import java.util.Date

class MyPageFirstTabFragment : BaseFragment<FragmentMyPageFirstTabBinding>(
    FragmentMyPageFirstTabBinding::bind,
    R.layout.fragment_my_page_first_tab
) {
    private var favoriteMountainList = mutableListOf(
            Mountain(R.drawable.dummy1, "가야산", 6),
            Mountain(R.drawable.dummy2, "가리산", 3),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2)
        )

    private var historyMountainList = mutableListOf(
            MountainHistory(R.drawable.dummy1, "가야산", Date()),
            MountainHistory(R.drawable.dummy2, "가리산", Date()),
            MountainHistory(R.drawable.dummy3, "가리왕ssssssssss산", Date()),
            MountainHistory(R.drawable.dummy3, "가리왕산", Date()),
            MountainHistory(R.drawable.dummy3, "가리왕산", Date()),
            MountainHistory(R.drawable.dummy3, "가리왕산", Date()),
            MountainHistory(R.drawable.dummy3, "가리왕산", Date())
        )

    private lateinit var favoriteMountainAdapter : MyPageFirstTabFavoriteMountainListAdapter
    private lateinit var historyMountainAdapter : MyPageFirstTabHistoryMountainListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavoriteMountain.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).apply {
                isMeasurementCacheEnabled = false
            }
            favoriteMountainAdapter = MyPageFirstTabFavoriteMountainListAdapter()
            favoriteMountainAdapter.apply {
                submitList(favoriteMountainList)
            }
            adapter = favoriteMountainAdapter
            addItemDecoration(SpaceItemDecoration(30))
        }
        binding.rvMountainHistory.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).apply {
                isMeasurementCacheEnabled = false
            }
            historyMountainAdapter = MyPageFirstTabHistoryMountainListAdapter()
            historyMountainAdapter.apply {
                submitList(historyMountainList)
            }
            adapter = historyMountainAdapter
            addItemDecoration(SpaceItemDecoration(30))
        }
    }
}