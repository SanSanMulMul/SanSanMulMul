package com.sansantek.sansanmulmul.ui.view.hikingrecordingtab

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentHikingRecordingTabBinding


class HikingRecordingTabFragment : BaseFragment<FragmentHikingRecordingTabBinding>(
    FragmentHikingRecordingTabBinding::bind,
    R.layout.fragment_hiking_recording_tab
) {
    private var state = BEFORE_HIKING
    private var isHikingInfoViewExpanded = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), false) }

        initSharedPreference()
        initButton()
        initHikingInfoView()

    }

    override fun onResume() {
        super.onResume()
        // 재개되면 스톱워치 값 가져와서 재개
        val saved = sharedPreferences.getLong(SP_SPEND_TIME_KEY, 0)
        setChronometerTime(saved)
//        binding.hikingSpendTime.start()
    }

    private fun initHikingInfoView() {
        binding.layoutHikingInfo.setOnClickListener {
            when (isHikingInfoViewExpanded) {
                true -> {
                    binding.layoutStepCnt.visibility = View.GONE
                    binding.layoutCalorie.visibility = View.GONE
                    isHikingInfoViewExpanded = false
                }

                false -> {
                    binding.layoutStepCnt.visibility = View.VISIBLE
                    binding.layoutCalorie.visibility = View.VISIBLE
                    isHikingInfoViewExpanded = true
                }
            }
        }
    }

    private fun chronometerToMilliSeconds(chronometerTime: Long): Long {
        return (SystemClock.elapsedRealtime() - chronometerTime) / 1000
    }

    private fun saveSpendTime() {
        val elapsedSeconds = chronometerToMilliSeconds(binding.hikingSpendTime.base)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        if (sharedPreferences.contains(SP_SPEND_TIME_KEY)) {
            editor.putLong(SP_SPEND_TIME_KEY, elapsedSeconds);
        } else {
            editor.putLong(SP_SPEND_TIME_KEY, elapsedSeconds);
        }
        editor.apply()
    }

    private fun setChronometerTime(seconds: Long) {
        val baseTime = SystemClock.elapsedRealtime() - seconds * 1000
        binding.hikingSpendTime.base = baseTime
    }

    private fun initButton() {
        binding.btnHikingRecording.setOnClickListener {
            when (state) {
                BEFORE_HIKING -> {
                    changeHikingButton(binding.btnHikingRecording, HIKING)
                    binding.hikingSpendTime.base = SystemClock.elapsedRealtime()
                    binding.hikingSpendTime.start()
                    state = HIKING
                }

                HIKING -> {
                    changeHikingButton(binding.btnHikingRecording, AFTER_HIKING)
                    state = AFTER_HIKING
                }

                AFTER_HIKING -> {
                    changeHikingButton(binding.btnHikingRecording, BEFORE_HIKING)
                    binding.hikingSpendTime.stop()
                    saveSpendTime()
                    state = BEFORE_HIKING
                }
            }
        }

    }

    private fun changeHikingButton(button: AppCompatButton, toState: Int) {
        when (toState) {
            BEFORE_HIKING -> {
                button.backgroundTintList =
                    ContextCompat.getColorStateList(
                        requireActivity(),
                        R.color.hiking_recording_tab_button_pink
                    )
                button.text = "상행 시작"
            }

            HIKING -> {
                button.backgroundTintList =
                    ContextCompat.getColorStateList(
                        requireActivity(),
                        R.color.sansanmulmul_green
                    )
                button.text = "하행 시작"
            }

            AFTER_HIKING -> {
                button.backgroundTintList =
                    ContextCompat.getColorStateList(
                        requireActivity(),
                        R.color.hiking_recording_tab_button_purple
                    )
                button.text = "끝내기"
            }
        }
    }

    private fun initSharedPreference() {
        sharedPreferences =
            requireActivity().getSharedPreferences("SanSanMulMulSharedPreference", MODE_PRIVATE)
    }

    companion object {
        private const val BEFORE_HIKING = 0
        private const val HIKING = 1
        private const val AFTER_HIKING = 2
        private const val SP_SPEND_TIME_KEY = "spend_time"
    }
}