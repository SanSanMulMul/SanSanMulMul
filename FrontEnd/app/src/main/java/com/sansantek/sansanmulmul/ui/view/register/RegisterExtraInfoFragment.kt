package com.sansantek.sansanmulmul.ui.view.register

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.databinding.FragmentRegisterExtraInfoBinding


class RegisterExtraInfoFragment : Fragment() {

  val binding by lazy {
    FragmentRegisterExtraInfoBinding.inflate(layoutInflater)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    setupSpinners()
    setGradient(binding.extraInfoText1)
    setGradient(binding.extraInfoText2)
    return binding.root
  }

  private fun setGradient(textView: TextView) {
    val paint = textView.paint
    val width = paint.measureText(textView.text.toString())

    val textShader = LinearGradient(
      0f, 0f, width, textView.textSize,
      intArrayOf(
        getColor(requireActivity(), R.color.gradientStartColor),
        getColor(requireActivity(), R.color.gradientEndColor)
      ), arrayOf(0f, 1f).toFloatArray(), Shader.TileMode.CLAMP
    )
    textView.paint.shader = textShader
  }


  private fun setupSpinners() {

    val yearSpinner: Spinner = binding.yearSpinner
    val monthSpinner: Spinner = binding.monthSpinner
    val daySpinner: Spinner = binding.daySpinner

    // 년도 Spinner
    val years = (1923..2023).toList()
    val yearAdapter =
      ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, years)
    yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    yearSpinner.adapter = yearAdapter
    yearSpinner.setSelection(77, true) // 2000으로 설정

    // 월 Spinner
    val months = (1..12).toList()
    val monthAdapter =
      ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, months)
    monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    monthSpinner.adapter = monthAdapter

    // 날짜 Spinner 초기화
    updateDays(daySpinner, yearSpinner.selectedItem as Int, monthSpinner.selectedItem as Int)

    // 년도 선택 이벤트 처리
    yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
      ) {
        updateDays(
          daySpinner,
          yearSpinner.selectedItem as Int,
          monthSpinner.selectedItem as Int
        )
        if (position == 0) {
          yearSpinner.setSelection(77, true) // 2000으로 설정
        }
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    // 월 선택 이벤트 처리
    monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
      ) {
        updateDays(
          daySpinner,
          yearSpinner.selectedItem as Int,
          monthSpinner.selectedItem as Int
        )
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
  }

  // 월별 날짜 수, 윤년 고려해서 2월 날짜 수
  private fun updateDays(daySpinner: Spinner, year: Int, month: Int) {
    val daysInMonth = when (month) {
      1, 3, 5, 7, 8, 10, 12 -> 31
      4, 6, 9, 11 -> 30
      2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
      else -> 31
    }
    val days = (1..daysInMonth).toList()
    val dayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, days)
    dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    daySpinner.adapter = dayAdapter
  }
}
