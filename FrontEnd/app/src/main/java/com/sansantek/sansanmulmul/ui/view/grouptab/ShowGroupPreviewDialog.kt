package com.sansantek.sansanmulmul.ui.view.grouptab

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.config.Const.Companion.HIKINGSTYLE
import com.sansantek.sansanmulmul.config.Const.Companion.TITLE
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.databinding.DialogGroupPreviewBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.layoutmanager.CustomLayoutmanager
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.crewService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

private const val TAG = "ShowGroupPreviewDialog_싸피"
class ShowGroupPreviewDialog(private val crew: Crew) : DialogFragment() {
  // 뷰 바인딩 정의
  private var _binding: DialogGroupPreviewBinding? = null
  private val binding get() = _binding!!
  private var styleList = listOf<String>()
  private lateinit var groupHikingStyleListAdapter: GroupHikingStyleListAdapter
  private val viewModel : MainActivityViewModel by viewModels()
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = DialogGroupPreviewBinding.inflate(inflater, container, false)

    Glide.with(binding.root).load(crew.mountainImg).into(binding.ivGroupPreview)

    binding.tvGroupTitle.text = crew.crewName
    binding.groupPersonInfo.text = "${crew.crewCurrentMembers} / ${crew.crewMaxMembers}"

    styleList = crew.crewStyles.map {
      HIKINGSTYLE[it]
    }

    binding.tvGroupLeaderTitle.text = TITLE[crew.userStaticBadge]
    binding.tvGroupLeaderName.text = crew.userNickname
    Glide.with(binding.root).load(crew.userProfileImg).into(binding.ivGroupLeaderImg)

    binding.ivGroupPreview.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.SRC_OVER)

    groupHikingStyleListAdapter = GroupHikingStyleListAdapter()

    binding.rvGridHikingStyle.apply {
      layoutManager = CustomLayoutmanager(requireContext(), 2)
      adapter = groupHikingStyleListAdapter
      groupHikingStyleListAdapter.submitList(crew.crewStyles)
    }
    val view = binding.root

    binding.acbBtnClose.setOnClickListener {
      dismiss()
    }

    binding.acbBtnRegister.setOnClickListener {
      viewModel.token?.let {
        lifecycleScope.launch {
          val result = crewService.registerCrew(makeHeaderByAccessToken(it.accessToken), crew.crewId)
          dismiss()
          if(result.isSuccessful){
            Toast.makeText(requireContext(), "가입 신청이 성공적으로 완료되었습니다! 방장의 승인을 기다려주세요!", Toast.LENGTH_SHORT).show()
            ShowGroupRegisterSuccessDialog().show(requireActivity().supportFragmentManager, "dialog")
          }else{
            AlertAlreadyRegisterGroupDialog().show(requireActivity().supportFragmentManager, "dialog")
          }
        }
      }
    }

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