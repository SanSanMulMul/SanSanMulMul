package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.Weather
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabSecondHikingInfoFragmentBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailTabWeatherInfoListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.DiviiderItemDecorator
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "GroupDetailTabSecondHikingInfoFragment_싸피"
class GroupDetailTabSecondHikingInfoFragment(private val crew: Crew) : BaseFragment<FragmentGroupDetailTabSecondHikingInfoFragmentBinding>(
    FragmentGroupDetailTabSecondHikingInfoFragmentBinding::bind,
    R.layout.fragment_group_detail_tab_second_hiking_info_fragment
) {
    private lateinit var weatherAdapter: GroupDetailTabWeatherInfoListAdapter
    private val weatherList = mutableListOf(Weather(getCurrentDate(0), "맑음", 20, 12), Weather(getCurrentDate(1), "강수", 17, 6), Weather(getCurrentDate(2), "흐림", 12, 6), Weather(getCurrentDate(3), "맑음", 28, 22), Weather(getCurrentDate(4), "맑음", 35, 26))
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkSelectedTabAndSetTabBG()
        binding.cvLayoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: ${binding.cvLayoutTab.selectedTabPosition}")
                checkSelectedTabAndSetTabBG()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: ${binding.cvLayoutTab.selectedTabPosition}")
                checkSelectedTabAndSetTabBG()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                checkSelectedTabAndSetTabBG()
            }
        })
        weatherAdapter = GroupDetailTabWeatherInfoListAdapter()
        binding.rvWeatherInfo.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DiviiderItemDecorator(ContextCompat.getDrawable(requireContext(),R.drawable.divider)!!))
            adapter = weatherAdapter
            weatherAdapter.submitList(weatherList)
        }
    }

    fun formatDate(date: Date): String {
        // SimpleDateFormat을 이용하여 "MM/dd" 형식으로 포맷팅
        val dateFormat = SimpleDateFormat("MM/dd")
        return dateFormat.format(date)
    }

    fun getCurrentDate(addDay: Int): Date {
        // Calendar 인스턴스를 통해 현재 날짜 및 시간 가져오기
        val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, addDay) }
        return calendar.time
    }

    private fun checkSelectedTabAndSetTabBG(){
        if(binding.cvLayoutTab.selectedTabPosition == 0){
            setTabBG(R.drawable.group_detail_second_tab_tabbar_left_selected, R.drawable.group_detail_second_tab_tabbar_right_unselected)
        }
        else{
            setTabBG(R.drawable.group_detail_second_tab_tabbar_left_unselected, R.drawable.group_detail_second_tab_tabbar_right_selected)
        }
    }
    private fun setTabBG(tab1: Int, tab2: Int) {
        val tabStrip = binding.cvLayoutTab
        val tabView1 = tabStrip.getTabAt(0)?.view
        val tabView2 = tabStrip.getTabAt(1)?.view

        tabView1?.setBackgroundResource(tab1)
        tabView2?.setBackgroundResource(tab2)
    }
}