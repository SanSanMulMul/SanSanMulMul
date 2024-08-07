package com.sansantek.sansanmulmul.ui.view.hikingrecordingtab

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.databinding.DialogCameraGuideBinding
import com.sansantek.sansanmulmul.ui.view.MainActivity

class CameraGuideDialog : DialogFragment() {
    private var _binding: DialogCameraGuideBinding? = null
    private val binding get() = _binding!!

    private lateinit var rootActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCameraGuideBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHighlightText()

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.tvPeakStoneDescription3.setOnClickListener {
            if (binding.ivExample.visibility == View.INVISIBLE) {
                binding.ivExample.visibility = View.VISIBLE
            } else {
                binding.ivExample.visibility = View.INVISIBLE
            }
        }
        binding.btnCamera.setOnClickListener {
            rootActivity.checkPermission()
            dismiss()
        }
    }

    private fun initHighlightText() {
        // SpannableString 생성
        val spannableString = SpannableString(binding.tvPeakStoneDescription1.text)

        // "정면"이라는 단어의 시작 인덱스와 끝 인덱스
        val start = binding.tvPeakStoneDescription1.text.indexOf("정면")
        val end = start + "정면".length

        // 특정 단어의 색상을 변경
        if (start >= 0) {
            spannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.sansanmulmul_green)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // 변경된 텍스트를 TextView에 설정
        binding.tvPeakStoneDescription1.text = spannableString
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            rootActivity = context
        }
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = getScreenWidth(this.requireContext())
        val screenHeight = getScreenHeight(this.requireContext())

        val newWidth = (screenWidth * 0.85).toInt()
        val newHeight = (screenHeight * 0.75).toInt()
        val layoutParams = requireView().layoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        requireView().layoutParams = layoutParams
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}