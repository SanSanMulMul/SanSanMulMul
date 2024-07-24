package com.sansantek.sansanmulmul.ui.view.hometab

import android.os.Bundle
import android.view.View
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentHomeTabBinding


class HomeTabFragment : BaseFragment<FragmentHomeTabBinding>(
    FragmentHomeTabBinding::bind,
    R.layout.fragment_home_tab
) {

    private var selectedIndex: Int = 0;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val motionLayout = binding.layoutCarouselNews

        val v1 = binding.root.findViewById<View>(R.id.v1)
        val v2 = binding.root.findViewById<View>(R.id.v2)
        val v3 = binding.root.findViewById<View>(R.id.v3)

        v1.setOnClickListener {
            if (selectedIndex == 0) return@setOnClickListener

            motionLayout.setTransition(R.id.s2, R.id.s1) //orange to blue transition
            motionLayout.transitionToEnd()
            selectedIndex = 0;
        }
        v2.setOnClickListener {
            if (selectedIndex == 1) return@setOnClickListener

            if (selectedIndex == 2) {
                motionLayout.setTransition(R.id.s3, R.id.s2)  //red to orange transition
            } else {
                motionLayout.setTransition(R.id.s1, R.id.s2) //blue to orange transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 1;
        }
        v3.setOnClickListener {
            if (selectedIndex == 2) return@setOnClickListener

            motionLayout.setTransition(R.id.s2, R.id.s3) //orange to red transition
            motionLayout.transitionToEnd()
            selectedIndex = 2;
        }
    }

}