package com.sansantek.sansanmulmul.ui.view.hikingrecordingtab

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.config.Const.Companion.AFTER_HIKING
import com.sansantek.sansanmulmul.config.Const.Companion.BEFORE_HIKING
import com.sansantek.sansanmulmul.config.Const.Companion.HIKING
import com.sansantek.sansanmulmul.data.local.SharedPreferencesUtil
import com.sansantek.sansanmulmul.databinding.FragmentHikingRecordingTabBinding
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.viewmodel.ChronometerViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.HikingRecordingTabViewModel


private const val TAG = "산산물물_HikingRecordingTabFragment"

class HikingRecordingTabFragment : BaseFragment<FragmentHikingRecordingTabBinding>(
    FragmentHikingRecordingTabBinding::bind,
    R.layout.fragment_hiking_recording_tab
) {
    private var isHikingInfoViewExpanded = false
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    private lateinit var hikingRecordingTabViewModel: HikingRecordingTabViewModel
    private lateinit var chronometerViewModel: ChronometerViewModel
    private var rootActivity: MainActivity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        showQRCodeDialog()
    }

    private fun init() {
        activity?.let {
            hideBottomNav(
                it.findViewById(R.id.main_layout_bottom_navigation),
                false
            )
        }

        sharedPreferencesUtil = SharedPreferencesUtil(requireContext())
        hikingRecordingTabViewModel = HikingRecordingTabViewModel(sharedPreferencesUtil)
        chronometerViewModel = ChronometerViewModel(sharedPreferencesUtil)

        initChronometer()
        initButtonClickListener()
        initHikingInfoViewClickListener()

        hikingRecordingTabViewModel.state.observe(viewLifecycleOwner) { state ->
            changeHikingButton(binding.btnHikingRecording, state)
        }


        // 일단은 banned 화면 클릭하면 없어지도록..
        binding.fragmentHikingRecordingLayoutBanned.setOnClickListener {
            binding.fragmentHikingRecordingLayoutBanned.visibility = View.GONE
            hikingRecordingTabViewModel.setState(BEFORE_HIKING)
        }
    }

    private fun showQRCodeDialog() {
        QRCodeDialog().show(requireActivity().supportFragmentManager, "dialog")
    }

    private fun initButtonClickListener() {
        binding.btnHikingRecording.setOnClickListener {
            val currentState = hikingRecordingTabViewModel.state.value
            Log.d(TAG, "initButtonClickListener: $currentState")
            when (currentState) {
                BEFORE_HIKING -> {
                    binding.hikingSpendTime.base = SystemClock.elapsedRealtime()
                    saveSpendTime()
                    binding.hikingSpendTime.start()
                    chronometerViewModel.startChronometer(SystemClock.elapsedRealtime())
                    hikingRecordingTabViewModel.setState(HIKING)
                }

                HIKING -> {
                    hikingRecordingTabViewModel.setState(AFTER_HIKING)
                }

                AFTER_HIKING -> {
                    binding.hikingSpendTime.stop()
                    setChronometerTime(0)
                    chronometerViewModel.stopChronometer()
                    hikingRecordingTabViewModel.setState(BEFORE_HIKING)
                    hikingRecordingTabViewModel.deleteState()
                }
            }
        }
        binding.btnCamera.setOnClickListener {
            rootActivity?.checkPermission()
        }
    }

    fun setImageBitmap(imageBitmap: Bitmap?) {
        binding.hikingRecordingTabMap.setImageBitmap(imageBitmap)
    }

    private fun initHikingInfoViewClickListener() {
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


    private fun initChronometer() {
        if (chronometerViewModel.isRunning()) {
            binding.hikingSpendTime.base = chronometerViewModel.getSpendTime()
            binding.hikingSpendTime.start()
        } else {
            binding.hikingSpendTime.base = SystemClock.elapsedRealtime()
        }
    }

    private fun saveSpendTime() {
        sharedPreferencesUtil.saveHikingRecordingTime(chronometerToMilliSeconds(binding.hikingSpendTime.base))
    }


    private fun setChronometerTime(seconds: Long) {
        val baseTime = SystemClock.elapsedRealtime() - seconds * 1000
        binding.hikingSpendTime.base = baseTime
    }

    private fun chronometerToMilliSeconds(chronometerTime: Long): Long {
        return (SystemClock.elapsedRealtime() - chronometerTime) / 1000
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            rootActivity = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        rootActivity = null
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume: ")
        if (chronometerViewModel.isRunning()) {
            binding.hikingSpendTime.base = chronometerViewModel.getSpendTime()
            binding.hikingSpendTime.start()
        }
        changeHikingButton(binding.btnHikingRecording, hikingRecordingTabViewModel.getState())
    }
}