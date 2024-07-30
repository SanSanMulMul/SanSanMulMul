package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.HikingStyle
import com.sansantek.sansanmulmul.databinding.FragmentMyPageEditBinding
import com.sansantek.sansanmulmul.ui.adapter.MyPageEditHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.MyPageHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.GridSpaceItemDecoration

class MyPageEditTabFragment : BaseFragment<FragmentMyPageEditBinding>(
    FragmentMyPageEditBinding::bind,
    R.layout.fragment_my_page_edit
) {
    private lateinit var hikeAdapter : MyPageEditHikingStyleListAdapter
    private var hikeStyleList = mutableListOf(HikingStyle("등산에 집중!", true), HikingStyle("등산도 식후경", false), HikingStyle("어쩌구", true), HikingStyle("저쩌구", false), HikingStyle("어쩌구저쩌구", false))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val myPageTitleItems = resources.getStringArray(R.array.my_page_title_spinner_menu_array)
        val myPageAdapter =
            ArrayAdapter(requireContext(), R.layout.item_search_spinner, myPageTitleItems)
        binding.spinnerTitle.adapter = myPageAdapter

        hikeAdapter = MyPageEditHikingStyleListAdapter()
        binding.rvHikeStyle.apply {
            itemAnimator = null
            adapter = hikeAdapter.apply {
                submitList(hikeStyleList)
                setItemClickListener(object : MyPageEditHikingStyleListAdapter.ItemClickListener{
                    override fun onClick(position: Int) {
                        hikeStyleList = hikeStyleList.mapIndexed { idx, it ->
                            if(idx == position){
                                HikingStyle(it.style, !it.check)
                            }
                            else{
                                HikingStyle(it.style, it.check)
                            }
                        }.toMutableList()
                        submitList(hikeStyleList)
                    }
                })
            }
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridSpaceItemDecoration(2, 60))
        }

        super.onViewCreated(view, savedInstanceState)

    }
}