package com.sansantek.sansanmulmul.ui.view.hometab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.FragmentMountainSearchResultBinding
import com.sansantek.sansanmulmul.ui.adapter.SearchResultOfMountainListAdapter
import com.sansantek.sansanmulmul.ui.view.mountaindetail.MountainDetailFragment

private const val TAG = "산산물물_MountainSearchResultFra"

class MountainSearchResultFragment : BaseFragment<FragmentMountainSearchResultBinding>(
    FragmentMountainSearchResultBinding::bind,
    R.layout.fragment_mountain_search_result
) {

    private var searchKeyword: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        if (bundle != null) {
            searchKeyword = bundle.getString("search_keyword")
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.layoutSearch.findViewById<EditText>(R.id.et_search).setText(searchKeyword)

        initSearchResultOfMountainListRecyclerView()
    }

    private fun initSearchResultOfMountainListRecyclerView() {
        val mountainList = initMountainData()
        val mountainRecyclerView = binding.rvMountain
        val mountainListAdapter = SearchResultOfMountainListAdapter(
            mountainList,
            object : SearchResultOfMountainListAdapter.OnItemClickListener {
                override fun onItemClick(mountain: Mountain) {
                    requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_view, MountainDetailFragment()).commit()
                }

            })

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