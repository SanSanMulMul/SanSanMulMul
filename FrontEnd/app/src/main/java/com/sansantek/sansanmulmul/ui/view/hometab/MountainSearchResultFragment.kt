package com.sansantek.sansanmulmul.ui.view.hometab

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.FragmentMountainSearchResultBinding
import com.sansantek.sansanmulmul.ui.adapter.SearchResultOfMountainListAdapter
import com.sansantek.sansanmulmul.ui.view.mountaindetail.MountainDetailFragment
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainSearchViewModel

private const val TAG = "번들"

class MountainSearchResultFragment : BaseFragment<FragmentMountainSearchResultBinding>(
    FragmentMountainSearchResultBinding::bind,
    R.layout.fragment_mountain_search_result
) {
    private lateinit var searchResult: List<Mountain>
    private val searchViewModel: MountainSearchViewModel by activityViewModels()
    private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()

    private val mountainListAdapter by lazy {
        SearchResultOfMountainListAdapter().apply {
            setItemClickListener(
                object : SearchResultOfMountainListAdapter.OnItemClickListener {
                    override fun onItemClick(mountain: Mountain) {
                        mountainDetailViewModel.setMountainID(mountain.mountainId)
                        changeFragmentWithSlideRightAnimation(MountainDetailFragment())
                    }
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 검색어 observe
        searchViewModel.searchKeyword.observe(viewLifecycleOwner) { keyword ->
            binding.layoutSearch.findViewById<EditText>(R.id.et_search).setText(keyword)
            searchViewModel.searchMountainList(keyword)
        }

        searchViewModel.mountainListWithCourses.observe(viewLifecycleOwner) { mountainWithCourses ->
            mountainListAdapter.submitList(mountainWithCourses)
        }

//        // 산 검색 결과 리스트 observe
//        searchViewModel.mountain.observe(viewLifecycleOwner) { mountains ->
//            if (mountains != null) {
//                this@MountainSearchResultFragment.searchResult = mountains
//                mountainListAdapter.submitList(searchResult)
//            } else {
//                Toast.makeText(context, searchViewModel.error.toString(), Toast.LENGTH_SHORT).show()
//            }
//        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()

    }

    private fun init() {
        binding.layoutSearch.findViewById<EditText>(R.id.et_search)
            .setText(searchViewModel.searchKeyword.value)
        initSearchResultOfMountainListRecyclerView()

        // 새로고침 깜박임 방지
        binding.rvMountain.itemAnimator = null

        // 새로 검색 시
        binding.editText.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                searchViewModel.setSearchKeyword(binding.editText.etSearch.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun initSearchResultOfMountainListRecyclerView() {
        val mountainRecyclerView = binding.rvMountain
        mountainRecyclerView.layoutManager = LinearLayoutManager(context)
        mountainRecyclerView.adapter = mountainListAdapter
    }


}