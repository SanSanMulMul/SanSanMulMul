package com.sansantek.sansanmulmul.ui.view.hometab

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.FragmentMountainSearchResultBinding
import com.sansantek.sansanmulmul.ui.adapter.SearchResultOfMountainListAdapter
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.mountaindetail.MountainDetailFragment
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainSearchViewModel
import kotlinx.coroutines.launch

private const val TAG = "번들"

class MountainSearchResultFragment : BaseFragment<FragmentMountainSearchResultBinding>(
    FragmentMountainSearchResultBinding::bind,
    R.layout.fragment_mountain_search_result
) {
    private val searchViewModel: MountainSearchViewModel by activityViewModels()
    private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private val mountainListAdapter by lazy {
        SearchResultOfMountainListAdapter().apply {
            setItemClickListener(
                object : SearchResultOfMountainListAdapter.OnItemClickListener {
                    override fun onItemClick(mountain: Mountain) {
                        mountainDetailViewModel.setMountainID(mountain.mountainId)
                        changeFragmentWithPopUpAnimation(MountainDetailFragment())
                    }

                    override fun onLikeClick(mountain: Mountain, check: Boolean) {
                        activityViewModel.token?.let {
                            if (check) {
                                lifecycleScope.launch {
                                    val result = mountainService.addLikeMountain(
                                        makeHeaderByAccessToken(it.accessToken),
                                        mountain.mountainId
                                    )
                                    if (result.isSuccessful) {
                                        if (result.body().equals("산 즐겨찾기 성공")) {
                                            showToast("즐겨찾기 등록 성공!")
                                        }
                                    } else {
                                        showToast("즐겨찾기 실패!")
                                        Log.d(TAG, "onLikeClick: 즐겨찾기 등록 오류")
                                    }
                                }
                            } else {
                                lifecycleScope.launch {
                                    val result = mountainService.deleteLikeMountain(
                                        makeHeaderByAccessToken(it.accessToken),
                                        mountain.mountainId
                                    )
                                    if (result.isSuccessful) {
                                        if (result.body().equals("즐겨찾기 제거")) {
                                            showToast("즐겨찾기 제거 성공!")
                                        }
                                    } else {
                                        showToast("즐겨찾기 실패!")
                                        Log.d(TAG, "onLikeClick: 즐겨찾기 제거 오류")
                                    }
                                }
                            }
                        }
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