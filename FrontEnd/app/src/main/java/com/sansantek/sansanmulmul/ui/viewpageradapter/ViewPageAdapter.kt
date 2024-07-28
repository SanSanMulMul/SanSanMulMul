package com.sansantek.sansanmulmul.ui.viewpageradapter

import GroupHikingStyleFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sansantek.sansanmulmul.ui.view.creategroup.DownCourseChoiceDialog
import com.sansantek.sansanmulmul.ui.view.creategroup.UpCourseChoiceDialog
import com.sansantek.sansanmulmul.ui.view.creategroup.GroupCourseSearchFragment
import com.sansantek.sansanmulmul.ui.view.creategroup.GroupCreateFinishFragment
import com.sansantek.sansanmulmul.ui.view.creategroup.GroupExtraInfoFragment
import com.sansantek.sansanmulmul.ui.view.creategroup.GroupIntroduceCreateFragment
import com.sansantek.sansanmulmul.ui.view.creategroup.GroupScheduleFragment

// 3개의 화면을 구성한다고 가정
// OneFragment, TwoFragment, ThreeFragment
class ViewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    // 페이지 갯수 설정
    override fun getItemCount(): Int = 6

    // 불러올 Fragment 정의
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GroupIntroduceCreateFragment()
            1 -> GroupExtraInfoFragment()
            2 -> GroupHikingStyleFragment()
            3 -> GroupCourseSearchFragment()
            4 -> GroupScheduleFragment()
            5 -> GroupCreateFinishFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}