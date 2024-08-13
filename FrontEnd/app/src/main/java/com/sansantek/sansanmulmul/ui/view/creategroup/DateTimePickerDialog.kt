package com.sansantek.sansanmulmul.ui.view.creategroup

// DateTimePickerDialog.kt
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.DialogDateTimePickerBinding
import com.sansantek.sansanmulmul.databinding.DialogGroupDowncourseChoiceBinding
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar

class DateTimePickerDialog(private val status: String) : DialogFragment() {

    // Callback 인터페이스
    interface DateTimeDialogListener {
        fun onDateTimeSelected(status: String, year: Int, month: Int, day: Int, hour: Int, minute: Int)
    }

    // Listener를 저장할 변수
    private var listener: DateTimeDialogListener? = null
    private var _binding: DialogGroupDowncourseChoiceBinding? = null
    private val binding get() = _binding!!

    // 프래그먼트에서 listener를 설정할 수 있도록 설정
    fun setDateTimeDialogListener(listener: DateTimeDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // 현재 날짜와 시간 가져오기
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // 다이얼로그 레이아웃 인플레이트
        val binding = DialogDateTimePickerBinding.inflate(LayoutInflater.from(activity))
        val datePicker = binding.datePicker
        val timePicker = binding.timePicker

        val positiveButton = binding.btnPositive
        val negativeButton = binding.btnNegative
        // 기본 값 설정

        val koreaZoneId = ZoneId.of("Asia/Seoul")
        val localDateTime = LocalDateTime.now(koreaZoneId)

        // LocalDateTime을 ZonedDateTime으로 변환
        val zonedDateTime = ZonedDateTime.of(localDateTime, koreaZoneId)

        // ZonedDateTime을 Instant로 변환
        val instant = zonedDateTime.toInstant()

        // Instant를 밀리초 단위의 타임스탬프로 변환
        val currentTimeMillis = instant.toEpochMilli()


        datePicker.updateDate(year, month, day)
        datePicker.minDate = currentTimeMillis
        timePicker.hour = 6
        timePicker.minute =0

        positiveButton.setOnClickListener {
            val selectedYear = datePicker.year
            val selectedMonth = datePicker.month
            val selectedDay = datePicker.dayOfMonth
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute

            // 선택한 날짜와 시간 전달
            listener?.onDateTimeSelected(status, selectedYear, selectedMonth+1, selectedDay, selectedHour, selectedMinute)
            dismiss() // 다이얼로그 닫기
        }
        negativeButton.setOnClickListener {
            dismiss()
        }
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    private fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    private fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    override fun onStart() {
        super.onStart()
//        val displayMetrics = DisplayMetrics()
//        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
//        val screenWidth = getScreenWidth(this.requireContext())
//        val screenHeight = getScreenHeight(this.requireContext())
//
//        val newWidth = (screenWidth * 0.95).toInt()
//        val newHeight = (screenHeight * 0.5).toInt()
//        val layoutParams = requireView().layoutParams
//        layoutParams.width = newWidth
//        layoutParams.height = newHeight
//        requireView().layoutParams = layoutParams
    }
}
