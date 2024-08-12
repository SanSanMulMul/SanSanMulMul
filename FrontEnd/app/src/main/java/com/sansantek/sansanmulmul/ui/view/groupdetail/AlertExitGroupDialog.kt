package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextPaint
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.databinding.DialogGroupExitBinding
import com.sansantek.sansanmulmul.databinding.DialogGroupRegisterBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.crewService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.view.grouptab.GroupTabFragment
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.runBlocking

class AlertExitGroupDialog(private val isLeader: Boolean, private val crew: Crew) : DialogFragment() {
    // 뷰 바인딩 정의
    private var _binding: DialogGroupExitBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGroupExitBinding.inflate(inflater, container, false)
        val view = binding.root
        if(isLeader){
            binding.tvAlert.text = "정말 삭제하시나요...?\n정말요.....?"
            binding.tvExitBtn.text = "삭제하기"
        }

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        // 닫기 버튼 클릭
        binding.tvExitBtn.paintFlags = TextPaint.UNDERLINE_TEXT_FLAG
        binding.tvExitBtn.setOnClickListener {
            if(isLeader){
                activityViewModel.token?.let {
                    runBlocking {
                        val result = crewService.deleteCrew(makeHeaderByAccessToken(it.accessToken), crew.crewId)
                        if(result.isSuccessful){
                            Toast.makeText(requireContext(), "성공적으로 그룹 삭제가 완료되었습니다!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else{
                activityViewModel.token?.let {
                    runBlocking {
                        val result = crewService.exitCrew(makeHeaderByAccessToken(it.accessToken), crew.crewId)
                        if(result.isSuccessful){
                            Toast.makeText(requireContext(), "성공적으로 그룹 탈퇴가 완료되었습니다!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            val activity = requireActivity() as MainActivity
            dismiss()
            activity.changeFragment(GroupTabFragment())
        }
        binding.btnCancel.setOnClickListener{
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = getScreenWidth(this.requireContext())
        val screenHeight = getScreenHeight(this.requireContext())

        val newWidth = (screenWidth * 0.8).toInt()
        val newHeight = (screenHeight * 0.5).toInt()
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