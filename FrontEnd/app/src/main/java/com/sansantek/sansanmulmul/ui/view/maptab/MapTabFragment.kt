package com.sansantek.sansanmulmul.ui.view.maptab

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.FragmentMapTabBinding


class MapTabFragment : BaseFragment<FragmentMapTabBinding>(
    FragmentMapTabBinding::bind,
    R.layout.fragment_map_tab
) {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), false) }
        initBottomSheet()

    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(createBottomSheetCallback())

//        initMountainListRecyclerView()
    }

    private fun initMountainListRecyclerView() {
        val mountainList = initMountainData()
        val mountainRecyclerView = binding.rvBottomSheetMountain
//        val mountainListAdapter = BottomSheetMountainListAdapter(
//            mountainList,
//            object : BottomSheetMountainListAdapter.OnItemClickListener {
//                override fun onItemClick(mountain: Mountain) {
//                    requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
//                        .replace(R.id.fragment_view, MountainDetailFragment()).commit()
//                }
//
//            })

        mountainRecyclerView.layoutManager = LinearLayoutManager(context)
//        mountainRecyclerView.adapter = mountainListAdapter

        val dividerDrawable = activity?.getDrawable(R.drawable.recyclerview_divider_lightgray)
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        if (dividerDrawable != null) dividerItemDecoration.setDrawable(dividerDrawable)
        mountainRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun initMountainData(): List<Mountain>? {
//        return listOf(
//            Mountain(R.drawable.dummy1, "가야산", 6),
//            Mountain(R.drawable.dummy2, "가리산", 3),
//            Mountain(R.drawable.dummy3, "가리왕산", 2),
//            Mountain(R.drawable.dummy3, "가리왕산", 2),
//            Mountain(R.drawable.dummy3, "가리왕산", 2),
//            Mountain(R.drawable.dummy3, "가리왕산", 2),
//            Mountain(R.drawable.dummy3, "가리왕산", 2)
//        )
        return null
    }

    private fun createBottomSheetCallback(): BottomSheetCallback {
        return object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {}
                    BottomSheetBehavior.STATE_EXPANDED -> {}
                    BottomSheetBehavior.STATE_COLLAPSED -> {}
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        }
    }
}