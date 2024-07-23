package com.sansantek.sansanmulmul.ui.view.grouptab

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
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
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sansantek.sansanmulmul.databinding.DialogGroupPreviewBinding
import com.sansantek.sansanmulmul.databinding.DialogGroupRegisterBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupHikingStyleListAdapter

class ShowGroupPreviewDialog : DialogFragment() {
    // 뷰 바인딩 정의
    private var _binding: DialogGroupPreviewBinding? = null
    private val binding get() = _binding!!
    private val styleList = mutableListOf("#등산도 식후경", "#등산에 집중","#어쩌구", "#저쩌구")
    private lateinit var adapter: GroupHikingStyleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGroupPreviewBinding.inflate(inflater, container, false)
        binding.ivGroupPreview.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.SRC_OVER)

        adapter = GroupHikingStyleListAdapter()
        binding.rvGridHikingStyle.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        binding.rvGridHikingStyle.adapter = adapter
        adapter.submitList(styleList)

        val view = binding.root


        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        // 닫기 버튼 클릭

        return view
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = getScreenWidth(this.requireContext())
        val screenHeight = getScreenHeight(this.requireContext())

        val newWidth = (screenWidth * 0.8).toInt()
        val newHeight = (screenHeight * 0.8).toInt()
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