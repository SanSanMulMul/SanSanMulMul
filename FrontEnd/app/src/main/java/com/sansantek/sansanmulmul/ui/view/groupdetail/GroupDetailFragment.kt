package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Alarm
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailBinding
import com.sansantek.sansanmulmul.databinding.PopupGroupDetailDrawerBinding
import com.sansantek.sansanmulmul.databinding.PopupGroupDetailNotiBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailAlarmListAdapter
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailDrawerListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.DividerItemDecorator
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.crewService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.view.groupchat.GroupChatFragment
import com.sansantek.sansanmulmul.ui.viewmodel.GroupDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private const val TAG = "GroupTabFragment 싸피"

class GroupDetailFragment(private val crew: Crew) : BaseFragment<FragmentGroupDetailBinding>(
    FragmentGroupDetailBinding::bind,
    R.layout.fragment_group_detail
) {
    private var popupShow = false
    private var drawerShow = false
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: GroupDetailViewModel by activityViewModels()
    private lateinit var popUp: PopupWindow
    private lateinit var drawerUp: PopupWindow
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popupShow = false
        Glide.with(binding.root).load(crew.mountainImg).into(binding.ivCrewMountainImg)
        binding.tvGroupTitle.text = crew.crewName
        val startDate = formatToCustomPattern(crew.crewStartDate)
        val endDate = formatToCustomPattern(crew.crewEndDate)
        binding.tvGroupSchedule.text = "$startDate - $endDate"
        binding.tvGroupPerson.text = "${crew.crewCurrentMembers} / ${crew.crewMaxMembers}"

        lifecycleScope.launch {
            activityViewModel.token?.let {
                val result = crewService.getCrewGalleryList(
                    makeHeaderByAccessToken(it.accessToken),
                    crew.crewId
                )
                if (result.isSuccessful) {
                    viewModel.setPictureList(result.body()!!)
                } else {
                    Log.d(TAG, "onViewCreated: 갤러리 정보 가저오기 에러")
                }
            }
        }


        changeGroupDetailFragmentView(GroupDetailTabFirstInfoFragment(crew))
        binding.layoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: ${tab?.position}")
                when (tab?.position) {
                    0 -> {
                        Log.d(TAG, "onTabSelected: groupInfoTab")
                        changeGroupDetailFragmentView(GroupDetailTabFirstInfoFragment(crew))
                    }

                    1 -> {
                        Log.d(TAG, "onTabSelected: hikingInfoTab")
                        changeGroupDetailFragmentView(GroupDetailTabSecondHikingInfoFragment(crew))
                    }

                    2 -> {
                        Log.d(TAG, "onTabSelected: galleryTab")
                        changeGroupDetailFragmentView(GroupDetailTabThirdGalleryInfoFragment(crew))
                    }

                    else -> {
                        Log.d(TAG, "onTabSelected: else")
                        changeGroupDetailFragmentView(GroupDetailTabThirdGalleryInfoFragment(crew))
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        binding.ibDrawer.setOnClickListener {
            lifecycleScope.launch {
                if (drawerShow) {
                    drawerUp.dismiss()
                    drawerShow = !drawerShow
                } else {
                    val screenWidth = getScreenWidth(requireContext())
                    val newWidth = (screenWidth * 0.3).toInt()
                    val newHeight = (screenWidth * 0.15).toInt()
                    val layoutInflater =
                        requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val location = IntArray(2)
                    val point = Point()
                    val drawerMenuList = mutableListOf(
                        Pair(R.drawable.remove_group, "그룹 삭제"),
                    )

                    val windowupBinding = PopupGroupDetailDrawerBinding.inflate(layoutInflater)
                    drawerUp = PopupWindow(requireContext()).apply {
                        contentView = windowupBinding.root
                        width = newWidth
                        height = newHeight
                        animationStyle = R.style.popup_window_animation
                        setBackgroundDrawable(ColorDrawable())
                    }

                    val windowDrawerListAdapter = GroupDetailDrawerListAdapter()

                    binding.layoutChatBtn.getLocationOnScreen(location)
                    point.x = location[0]
                    point.y = location[1]

                    drawerUp.showAtLocation(
                        windowupBinding.root,
                        Gravity.NO_GRAVITY,
                        screenWidth - (screenWidth - newWidth) / 2,
                        point.y + binding.ibDrawer.height + 20
                    )

                    windowupBinding.rvGroupDetailDrawerList.apply {
                        adapter = windowDrawerListAdapter.apply {
                            submitList(drawerMenuList)
                            setItemClickListener(object :
                                GroupDetailDrawerListAdapter.ItemClickListener {


                                override fun onExitGroupClick() {
                                    activityViewModel.token?.let {
                                        lifecycleScope.launch {
                                            val result = crewService.getCrewCommon(
                                                makeHeaderByAccessToken(it.accessToken),
                                                crew.crewId
                                            )
                                            if (result.isSuccessful) {
                                                AlertExitGroupDialog(
                                                    result.body()!!.leader,
                                                    crew
                                                ).show(childFragmentManager, "dialog")
                                            }
                                        }
                                    }
                                }

                            })
                        }
                        layoutManager = LinearLayoutManager(requireContext())
                        addItemDecoration(
                            DividerItemDecorator(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.divider
                                )!!
                            )
                        )
                    }
                    drawerShow = !drawerShow
                }
            }
        }

        binding.ibNoti.setOnClickListener {
            lifecycleScope.launch {

                if (popupShow) {
                    popUp.dismiss()
                    popupShow = !popupShow
                } else {
                    val screenWidth = getScreenWidth(requireContext())
                    val newWidth = (screenWidth * 0.8).toInt()
                    val newHeight = (screenWidth * 0.95).toInt()
                    val popUpList = mutableListOf(
                        Alarm(
                            "등산 일정 변경",
                            "등산 일정이 24.07.18(목) 13:00 - 24.07.19(금) 14:00로 변경되었습니다"
                        ),
                        Alarm("그룹 가입 요청", "nickname 님이 그룹 가입을 요청했습니다! 멤버 목록에서 수락 또는 거절할 수 있습니다"),
                        Alarm("등산 코스 변경", "등산 코스가 가야산 / 가야산국립공원남산제일봉2코스로 변경되었습니다"),
                        Alarm("그룹 가입 요청", "박태우 님이 그룹 가입을 요청했습니다! 멤버 목록에서 수락 또는 거절할 수 있습니다"),
                    )
                    val popUpAlarmListAdapter = GroupDetailAlarmListAdapter()
                    val layoutInflater =
                        requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val location = IntArray(2)
                    val point = Point()

                    val popupBinding = PopupGroupDetailNotiBinding.inflate(layoutInflater)
                    popUp = PopupWindow(requireContext()).apply {
                        contentView = popupBinding.root
                        width = newWidth
                        height = newHeight
                        animationStyle = R.style.popup_window_animation
                        setBackgroundDrawable(ColorDrawable())
                    }
                    popupBinding.btnRemovePopup.setOnClickListener {
                        popUp.dismiss()
                        popupShow = !popupShow
                    }

                    binding.layoutChatBtn.getLocationOnScreen(location)
                    point.x = location[0]
                    point.y = location[1]
                    popUp.showAtLocation(
                        popupBinding.root,
                        Gravity.NO_GRAVITY,
                        (screenWidth - newWidth) / 2,
                        point.y + binding.layoutChatBtn.height + 10
                    )

                    Log.d(TAG, "onViewCreated: 팝업 리사이클러 뷰 실행 직전")
                    popupBinding.rvAlarmList.apply {
                        adapter = popUpAlarmListAdapter.apply { submitList(popUpList) }
                        layoutManager = LinearLayoutManager(requireContext())
                        addItemDecoration(
                            DividerItemDecorator(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.divider
                                )!!
                            )
                        )
                    }

                    Log.d(TAG, "onViewCreated: 팝업 리사이클러 뷰 실행 후")

                    popupShow = !popupShow
                }
            }
        }

        binding.ibBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 채팅 버튼 클릭 시 GroupChatFragment로 전환
        binding.layoutChatBtn.setOnClickListener {
            val activity = requireActivity() as MainActivity
            activity.changeAddToBackstackFragment(GroupChatFragment(crew))
        }
    }

    override fun onPause() {
        if (popupShow) {
            popUp.dismiss()
            popupShow = !popupShow
        }
        if (drawerShow) {
            drawerUp.dismiss()
            drawerShow = !drawerShow
        }
        super.onPause()
    }

    fun changeGroupDetailFragmentView(view: Fragment) {
        this.childFragmentManager.beginTransaction()
            .replace(binding.groupDetailTabFragmentView.id, view).commit()
    }

    fun changeAddToBackStackGroupDetailFragmentView(view: Fragment) {
        this.childFragmentManager.beginTransaction()
            .replace(binding.groupDetailTabFragmentView.id, view).addToBackStack(null).commit()
    }

    fun popBackStackGroupDetailFragmentView() {
        this.childFragmentManager.popBackStack()
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

    private fun formatToCustomPattern(isoDateTime: String): String {
        // ISO 8601 형식의 문자열을 ZonedDateTime으로 파싱
        val localDateTime = LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_DATE_TIME)

        // 원하는 형식으로 변환
        val formattedDate = localDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd"))
        val dayOfWeek = localDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
        val time = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        // 최종 포맷팅된 문자열 반환
        return "$formattedDate($dayOfWeek) $time"
    }
}