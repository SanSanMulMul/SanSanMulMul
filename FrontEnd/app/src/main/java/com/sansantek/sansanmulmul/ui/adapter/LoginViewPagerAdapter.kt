package com.sansantek.sansanmulmul.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sansantek.sansanmulmul.ui.view.creategroup.CourseChoiceDialog
import com.sansantek.sansanmulmul.ui.view.creategroup.GroupCourseSearchFragment
import com.sansantek.sansanmulmul.ui.view.creategroup.GroupExtraInfoFragment
import com.sansantek.sansanmulmul.ui.view.creategroup.GroupIntroduceCreateFragment
import com.sansantek.sansanmulmul.ui.view.groupdetail.GroupDetailFragment
import com.sansantek.sansanmulmul.ui.view.register.RegisterExtraInfoFragment
import com.sansantek.sansanmulmul.ui.view.register.RegisterFinishFragment
import com.sansantek.sansanmulmul.ui.view.register.RegisterHikingStyleFragment
import com.sansantek.sansanmulmul.ui.view.register.RegisterProfileFragment
import com.sansantek.sansanmulmul.ui.view.register.RegisterStartFragment

// 3개의 화면을 구성한다고 가정
// OneFragment, TwoFragment, ThreeFragment
class LoginViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
class ViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    // 페이지 갯수 설정
    override fun getItemCount(): Int = 5

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