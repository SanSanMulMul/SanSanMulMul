package com.sansantek.sansanmulmul.ui.view.grouptab

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout.VERTICAL
import androidx.recyclerview.widget.DividerItemDecoration
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.GroupListInfo
import com.sansantek.sansanmulmul.databinding.FragmentGroupTabBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupTabListAdapter

class GroupTabFragment : BaseFragment<FragmentGroupTabBinding>(FragmentGroupTabBinding::bind, R.layout.fragment_group_tab) {
    private val groupListInfoList = mutableListOf<GroupListInfo>()
    private lateinit var groupTabListAdapter: GroupTabListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchItems = resources.getStringArray(R.array.grouptab_search_spinner_menu_array)
        val ageItems = resources.getStringArray(R.array.grouptab_age_spinner_menu_array)
        val styleItems = resources.getStringArray(R.array.grouptab_style_spinner_menu_array)
        val genderItems = resources.getStringArray(R.array.grouptab_gender_spinner_menu_array)
        val groupItems = resources.getStringArray(R.array.grouptab_group_spinner_menu_array)

        val searchAdapter = ArrayAdapter(requireContext(), R.layout.item_search_spinner, searchItems)
        val ageAdapter = ArrayAdapter(requireContext(), R.layout.item_filter_spinner, ageItems)
        val styleAdapter = ArrayAdapter(requireContext(), R.layout.item_filter_spinner, styleItems)
        val genderAdapter = ArrayAdapter(requireContext(), R.layout.item_filter_spinner, genderItems)

        val groupAdapter = ArrayAdapter(requireContext(), R.layout.item_group_spinner, groupItems)

        binding.searchSpinner.adapter = searchAdapter

        binding.ageSpinner.adapter = ageAdapter
        binding.styleSpinner.adapter = styleAdapter
        binding.genderSpinner.adapter = genderAdapter

        binding.myGroupSpinner.adapter = groupAdapter

        for(i in 1..15){
            groupListInfoList.add(GroupListInfo("가야산을 사랑하는 한사랑 산악회의 소모임입니다" ))
        }
        groupTabListAdapter = GroupTabListAdapter()
        binding.groupList.adapter = groupTabListAdapter
        groupTabListAdapter.submitList(groupListInfoList)

        val decoration = DividerItemDecoration(requireContext(), VERTICAL)
        binding.groupList.addItemDecoration(decoration)

        binding.searchSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when (position) {
                    0 -> {

                    }

                    1 -> {

                    }
                    //...
                    else -> {

                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}