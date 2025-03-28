package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sansantek.sansanmulmul.ui.view.register.RegisterExtraInfoFragment
import com.sansantek.sansanmulmul.ui.view.register.RegisterFinishFragment
import com.sansantek.sansanmulmul.ui.view.register.RegisterHikingStyleFragment
import com.sansantek.sansanmulmul.ui.view.register.RegisterProfileFragment

// 3개의 화면을 구성한다고 가정
// OneFragment, TwoFragment, ThreeFragment
class LoginViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    // 페이지 갯수 설정
    override fun getItemCount(): Int = 4

    // 불러올 Fragment 정의
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RegisterExtraInfoFragment()
            1 -> RegisterProfileFragment()
            2 -> RegisterHikingStyleFragment()
            3 -> RegisterFinishFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}