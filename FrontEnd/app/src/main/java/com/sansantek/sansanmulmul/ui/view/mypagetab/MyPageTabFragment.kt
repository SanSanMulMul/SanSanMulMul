package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMyPageTabBinding
import com.sansantek.sansanmulmul.ui.adapter.MyPageHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.layoutmanager.CustomLayoutmanager
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.convertHikingStyleIntListToStringList
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MyPageTabFragment_싸피"

class MyPageTabFragment : BaseFragment<FragmentMyPageTabBinding>(
    FragmentMyPageTabBinding::bind,
    R.layout.fragment_my_page_tab
) {
    private var styleList: List<String> = mutableListOf()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var activity: MainActivity
    private lateinit var myPageHikingStyleListAdapter: MyPageHikingStyleListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: view created 실행됨")
        init()
        initClickListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initClickListener() {
        binding.btnEditProfile.setOnClickListener {
            activity.changeAddToBackstackFragment(MyPageEditTabFragment())
        }

        // 팔로워 리스트로 이동
        binding.layoutMyFollowerInfo.setOnClickListener {
            val followerFragment = MypageFollowListFragment.newInstance(isFollowerList = true)
            activity.changeAddToBackstackFragment(followerFragment)
        }

        // 팔로잉 리스트로 이동
        binding.layoutMyFollowingInfo.setOnClickListener {
            val followingFragment = MypageFollowListFragment.newInstance(isFollowerList = false)
            activity.changeAddToBackstackFragment(followingFragment)
        }

        binding.tlTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        replaceFragment(MyPageFirstTabFragment())
                    }

                    else -> {
                        replaceFragment(MyPageSecondTabFragment())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun init() {
        replaceFragment(MyPageFirstTabFragment())
        myPageHikingStyleListAdapter = MyPageHikingStyleListAdapter()
        lifecycleScope.launch {
            launch {
                if (!activityViewModel.isUserInitialized()) {
                    loadUserProfile()
                }
                Glide.with(binding.root).load(activityViewModel.user.userProfileImg)
                    .into(binding.ivMyPageProfile)
                if(activityViewModel.myPageInfo.value == null){
                    lifecycleScope.launch(Dispatchers.Main) {
                        activityViewModel.token?.let {
                            launch(Dispatchers.Main) {
                                val myPageInfo =
                                    userService.getMyPageInfo(makeHeaderByAccessToken(it.accessToken))
                                activityViewModel.setMyPageInfo(myPageInfo)
                            }
                        }
                    }
                }
                activityViewModel.myPageInfo.value?.let {
                    Log.d(TAG, "init: 이제서야 binding에 매핑 시작 $it")
                    binding.tvTitleName.text = it.userBadge
                    binding.tvUserName.text = it.userNickname
                    binding.tvFollowerCnt.text = it.followerCnt.toString()
                    binding.tvFollowingCnt.text = it.followingCnt.toString()
                }
            }
            launch {
                if (activityViewModel.hikingStyles.value.isNullOrEmpty()) {
                    loadUserHikingStyle()
                }
                activityViewModel.hikingStyles.value?.let {
                    styleList = convertHikingStyleIntListToStringList(it)
                }
                binding.rvMyHikingStyle.apply {
                    adapter = myPageHikingStyleListAdapter.apply {
                        submitList(styleList)
                    }
                    layoutManager = CustomLayoutmanager(requireContext(), 2)
                }
            }
        }
    }

    private suspend fun loadUserHikingStyle() {
        activityViewModel.token?.let {
            activityViewModel.setHikingStyles(
                userService.getHikingStyle(makeHeaderByAccessToken(it.accessToken)).sorted()
            )
        }
    }

    private suspend fun loadUserProfile() {
        activityViewModel.token?.let {
            val myPage = userService.getMyPageInfo(makeHeaderByAccessToken(it.accessToken))
            activityViewModel.setMyPageInfo(myPage)
            val user = userService.loadUserProfile(makeHeaderByAccessToken(it.accessToken))
            if (user.code() == 200) {
                user.body()?.let { result ->
                    Log.d(TAG, "loadUserProfile 뷰모델에 결과 적용: user: $result")
                    activityViewModel.setUser(result)
                }
            }
        }

    }

    fun replaceFragment(view: Fragment) {
        childFragmentManager.beginTransaction().replace(binding.myPageFragmentView.id, view)
            .commit()
    }
}