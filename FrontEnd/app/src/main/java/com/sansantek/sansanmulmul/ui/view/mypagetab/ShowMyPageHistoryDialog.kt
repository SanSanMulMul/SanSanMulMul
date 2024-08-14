package com.sansantek.sansanmulmul.ui.view.mypagetab

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.sansantek.sansanmulmul.data.model.HistoryMember
import com.sansantek.sansanmulmul.data.model.MountainHistory
import com.sansantek.sansanmulmul.databinding.DialogMyPageHistoryBinding
import com.sansantek.sansanmulmul.ui.adapter.MyPageHistoryMemberListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.SpaceItemDecoration

class ShowMyPageHistoryDialog(mountainHistory: MountainHistory) : DialogFragment(), OnMapReadyCallback {
    // 뷰 바인딩 정의
    private var _binding: DialogMyPageHistoryBinding? = null
    private val binding get() = _binding!!
    private val memberList = mutableListOf<HistoryMember>()
    private lateinit var myPageHistoryMemberListAdapter: MyPageHistoryMemberListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMyPageHistoryBinding.inflate(inflater, container, false)
        binding.ivGroupPreview.setColorFilter(
            Color.parseColor("#99000000"),
            PorterDuff.Mode.SRC_OVER
        )
        binding.ibCloseBtn.setOnClickListener { dismiss() }

        binding.tvMountainName.text

        for (i in 1..3) {
            val member = HistoryMember("엄홍길👑", "박태asssssssss우스")
            memberList.add(member)
        }
        for (i in 1..3) {
            val member = HistoryMember("엄홍길👑", "박태우스")
            memberList.add(member)
        }

        binding.navermapMapView.getMapAsync(this)


        myPageHistoryMemberListAdapter = MyPageHistoryMemberListAdapter()

        binding.rvMemberList.apply {
            adapter = myPageHistoryMemberListAdapter.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                ).apply {
                    isMeasurementCacheEnabled = false
                }
                addItemDecoration(SpaceItemDecoration(10))
                submitList((memberList))
            }
        }
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        // 닫기 버튼 클릭

        return view
    }

    override fun onMapReady(naverMap: NaverMap) {
        // 줌 버튼 비활성화
        val uiSettings: UiSettings = naverMap.uiSettings
        uiSettings.isZoomControlEnabled = false

        // 추가적으로 다른 설정을 할 수 있습니다.
        naverMap.moveCamera(CameraUpdate.zoomTo(10.0))
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = getScreenWidth(this.requireContext())
        val screenHeight = getScreenHeight(this.requireContext())

        val newWidth = (screenWidth * 0.8).toInt()
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