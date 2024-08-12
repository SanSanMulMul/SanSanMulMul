package com.sansantek.sansanmulmul.ui.view.hikingrecordingtab

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.sansantek.sansanmulmul.databinding.DialogLeaderQrCodeBinding
import com.sansantek.sansanmulmul.ui.viewmodel.HikingRecordingTabViewModel

private const val TAG = "싸피_QRCodeDialog"

class LeaderQRCodeDialog : DialogFragment() {
    private var _binding: DialogLeaderQrCodeBinding? = null
    private val binding get() = _binding!!
    private val hikingRecordingTabViewModel: HikingRecordingTabViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLeaderQrCodeBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnComplete.setOnClickListener {
            hikingRecordingTabViewModel.setIsQRCompleted(true)
            Log.d(TAG, "isQRCompleted: ${hikingRecordingTabViewModel.isQRCompleted.value}")
            dismiss()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createQRCode()  // QR코드 생성

    }


    private fun createQRCode() {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val qrBitmap = barcodeEncoder.encodeBitmap(
                "sansanmulmul://openFragment?fragment=HikingRecordingFragment", // 포함될 내용
                BarcodeFormat.QR_CODE, // 바코드 유형
                400,
                400
            )
            binding.qrCode.setImageBitmap(qrBitmap)
        } catch (e: Exception) {
            Toast.makeText(context, "QR Code 생성 실패!", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "createQRCode: QR Code 생성 실패!")
        }
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = getScreenWidth(this.requireContext())
        val screenHeight = getScreenHeight(this.requireContext())

        val newWidth = (screenWidth * 0.85).toInt()
        val newHeight = (screenHeight * 0.7).toInt()
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