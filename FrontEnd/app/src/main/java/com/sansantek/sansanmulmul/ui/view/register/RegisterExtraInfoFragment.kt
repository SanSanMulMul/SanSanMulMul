package com.sansantek.sansanmulmul.ui.view.register

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentRegisterExtraInfoBinding
import java.time.LocalDate
import java.util.Calendar

private const val TAG = "산산물물_RegisterExtraInfoFragment"

class RegisterExtraInfoFragment : BaseFragment<FragmentRegisterExtraInfoBinding>(
    FragmentRegisterExtraInfoBinding::bind,
    R.layout.fragment_register_extra_info
) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGradient(binding.extraInfoText1)
        setGradient(binding.extraInfoText2)
        setSpinner()
    }

    private fun setGradient(textView: TextView) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())

        val textShader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(
                getColor(requireActivity(), R.color.sansanmulmul_green),
                getColor(requireActivity(), R.color.gradientEndColor)
            ), arrayOf(0f, 1f).toFloatArray(), Shader.TileMode.CLAMP
        )
        textView.paint.shader = textShader
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setSpinner() {
        val year: NumberPicker = binding.npYear
        val month: NumberPicker = binding.npMonth
        val day: NumberPicker = binding.npDay
        val currentDate = getCurrentDate()

        Log.d(
            TAG,
            "setSpinner: ${currentDate.year}, ${currentDate.monthValue}, ${currentDate.dayOfMonth}"
        )
        initNumberPicker(year, 1900, maxVal = currentDate.year)
        initNumberPicker(month, minVal = 1, maxVal = 12)
        initNumberPicker(day, minVal = 1, maxVal = 31)

        year.value = currentDate.year
        month.value = currentDate.monthValue
        day.value = currentDate.dayOfMonth

        year.setOnValueChangedListener { _, _, newVal ->
            day.maxValue = updateDayPicker(newVal, month.value)
        }
        month.setOnValueChangedListener { _, _, newVal ->
            day.maxValue = updateDayPicker(year.value, newVal)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): LocalDate {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            LocalDate.of(year, month, day)
        }
    }

    private fun initNumberPicker(
        numberPicker: NumberPicker,
        minVal: Int,
        maxVal: Int
    ) {
        with(numberPicker) {
            wrapSelectorWheel = false
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            minValue = minVal
            maxValue = maxVal
        }
    }

    private fun updateDayPicker(year: Int, month: Int): Int {
        return when (month) {
            4, 6, 9, 11 -> 30
            2 -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28
            else -> 31
        }
    }

}
