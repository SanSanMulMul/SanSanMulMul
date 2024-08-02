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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainDto
import com.sansantek.sansanmulmul.databinding.FragmentMountainSearchResultBinding
import com.sansantek.sansanmulmul.ui.adapter.SearchResultOfMountainListAdapter
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.view.mountaindetail.MountainDetailFragment
import kotlinx.coroutines.launch

private const val TAG = "번들"

class MountainSearchResultFragment : BaseFragment<FragmentMountainSearchResultBinding>(
    FragmentMountainSearchResultBinding::bind,
    R.layout.fragment_mountain_search_result
) {
    private var searchKeyword: String? = null
    private lateinit var searchResult: List<MountainDto>
//    private lateinit var mountainListAdapter : SearchResultOfMountainListAdapter
    private val mountainListAdapter by lazy{
        SearchResultOfMountainListAdapter().apply {
            setItemClickListener(
                object : SearchResultOfMountainListAdapter.OnItemClickListener {
                    override fun onItemClick(mountain: MountainDto) {

                        // 산 상세 화면에 보낼 자료
                        val bundle = Bundle()
                        bundle.putInt("mountainId", mountain.mountainId)
                        bundle.putString("mountainName", mountain.mountainName)
                        bundle.putInt("mountainHeight", mountain.mountainHeight)
                        bundle.putString("mountainDescription", mountain.mountainDescription)
                        bundle.putString("mountainImage", mountain.mountainImg)

                        val mountainDetailFragment = MountainDetailFragment()

                        mountainDetailFragment.arguments = bundle
                        Log.d(TAG, "onItemClick: resuyltFragment 여기는 멀쩡? $mountain")


                        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                            .replace(R.id.fragment_view, mountainDetailFragment).commit()


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
        val bundle = arguments
        if (bundle != null) {
            searchKeyword = bundle.getString("search_keyword")
            // 비동기(lifecycleScope.launch)로 api 호출
            lifecycleScope.launch {
                // Null 값이 아니면
                searchKeyword?.let {

                    // searchResult 변수에 mountainService내에 searchMountainList 함수 호출한 결과 저장
                    // 인자는 searchKeyword(it)
                    val searchResult = mountainService.searchMountainList(it)
                    this@MountainSearchResultFragment.searchResult = searchResult

                    // submitList : 리스트 받는 함수
                    mountainListAdapter.submitList(searchResult)
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.layoutSearch.findViewById<EditText>(R.id.et_search).setText(searchKeyword)

        // 새로고침 깜박임 방지
        binding.rvMountain.itemAnimator = null

        // 새로 검색 할 시
        initSearchResultOfMountainListRecyclerView()
        binding.editText.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                lifecycleScope.launch {
                    //
                    arguments?.putString("search_keyword", binding.editText.etSearch.text.toString())
                    val searchNewResult = mountainService.searchMountainList(binding.editText.etSearch.text.toString())
                    Log.d(TAG, "onViewCreated: ${binding.editText.etSearch.text}      $searchNewResult")
                    mountainListAdapter.submitList(searchNewResult)
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun initSearchResultOfMountainListRecyclerView() {
        val mountainList = initMountainData()
        val mountainRecyclerView = binding.rvMountain
        mountainRecyclerView.layoutManager = LinearLayoutManager(context)
        mountainRecyclerView.adapter = mountainListAdapter
    }

    private fun initMountainData(): List<Mountain> {
        return listOf(
            Mountain(R.drawable.dummy1, "가야산", 6),
            Mountain(R.drawable.dummy2, "가리산", 3),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2)
        )
    }

}