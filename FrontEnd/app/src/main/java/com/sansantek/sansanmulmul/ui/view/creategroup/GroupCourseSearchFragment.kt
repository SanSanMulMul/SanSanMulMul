package com.sansantek.sansanmulmul.ui.view.creategroup

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupCourseSearchBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupCourceSearchListAdapter

class GroupCourseSearchFragment : BaseFragment<FragmentGroupCourseSearchBinding>(FragmentGroupCourseSearchBinding::bind, R.layout.fragment_group_course_search) {
    // 예시 데이터 리스트
    private var dataList = listOf("가야산", "가야산", "가야산", "가야산")

    private lateinit var adapter: GroupCourceSearchListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchView()

        adapter = GroupCourceSearchListAdapter()
        binding.rvListSearchMountain.adapter = adapter
        binding.rvListSearchMountain.layoutManager = LinearLayoutManager(requireContext())

        // 데이터를 어댑터에 전달
        adapter.submitList(dataList)
    }

    private fun initSearchView() {
        // SearchView 초기화
        val searchView = requireView().findViewById<SearchView>(R.id.sv_mountain_searchbar)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색어가 null이 아니고 비어있지 않은 경우
                if (!query.isNullOrEmpty()) {
                    // 검색어가 데이터 리스트에 존재하는지 확인
                    val isFound = dataList.contains(query)

                    // 검색어가 리스트에 있을 경우
                    if (isFound) {
                        // 검색어에 해당하는 항목만 필터링하여 어댑터에 제출
                        val filteredList = dataList.filter { it == query }
                        adapter.submitList(filteredList) // 검색어와 일치하는 항목만 표시
                        return true
                    } else {
                        // 검색어가 데이터 리스트에 없을 경우, 빈 목록을 어댑터에 제출
                        adapter.submitList(emptyList())
                        return true
                    }
                }
                // 검색어가 비어있거나 null일 경우 모든 항목 표시
                adapter.submitList(dataList)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the data based on the search query
                val filteredList = if (newText.isNullOrEmpty()) {
                    dataList // Show all if query is empty
                } else {
                    dataList.filter { it.contains(newText, ignoreCase = true) } // Filter the list
                }
                adapter.submitList(filteredList) // Submit the filtered list to adapter
                dataList = filteredList
                return true
            }
        })
    }
}