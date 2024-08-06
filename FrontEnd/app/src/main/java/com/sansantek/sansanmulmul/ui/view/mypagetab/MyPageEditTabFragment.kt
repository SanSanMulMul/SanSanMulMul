package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.config.Const.Companion.HIKINGSTYLE
import com.sansantek.sansanmulmul.config.Const.Companion.HIKINGSTYLESIZE
import com.sansantek.sansanmulmul.config.Const.Companion.TITLE
import com.sansantek.sansanmulmul.data.model.HikingStyle
import com.sansantek.sansanmulmul.data.model.ProfileUpdateData
import com.sansantek.sansanmulmul.databinding.FragmentMyPageEditBinding
import com.sansantek.sansanmulmul.ui.adapter.MyPageEditHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.GridSpaceItemDecoration
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "MyPageEditTabFragment 싸피"

class MyPageEditTabFragment : BaseFragment<FragmentMyPageEditBinding>(
    FragmentMyPageEditBinding::bind,
    R.layout.fragment_my_page_edit
) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private var hikeStyleList =
        mutableListOf<HikingStyle>() //mutableListOf(HikingStyle(HIKINGSTYLE[], true), HikingStyle("등산도 식후경", false), HikingStyle("어쩌구", true), HikingStyle("저쩌구", false), HikingStyle("어쩌구저쩌구", false))
    private lateinit var hikeAdapter: MyPageEditHikingStyleListAdapter
    private lateinit var titleList: List<String>
    private lateinit var myPageAdapter: ArrayAdapter<String>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spinnerTitle.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.let {
                    val item = it.getItemAtPosition(position)
                    Log.d(TAG, "onItemSelected: $item")
                    Log.d(TAG, "onItemSelected: ${TITLE.indexOf(item)}")
                    activityViewModel.setUserTitle(TITLE.indexOf(item))
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                val styleList = hikeStyleList.filter {
                    it.check
                }.map { style ->
                    HIKINGSTYLE.indexOf(style.style)
                }
                Log.d(TAG, "onViewCreated: styleList $styleList")
                activityViewModel.token?.let {
                    Log.d(
                        TAG,
                        "onViewCreated: update 내용 ${
                            ProfileUpdateData(
                                binding.etNickname.text.toString(),
                                activityViewModel.userProfileImgUrl,
                                activityViewModel.userTitle,
                                styleList
                            )
                        }"
                    )
                    val result = userService.updateUserProfile(
                        makeHeaderByAccessToken(it.accessToken),
                        ProfileUpdateData(
                            binding.etNickname.text.toString(),
                            activityViewModel.userProfileImgUrl,
                            activityViewModel.userTitle,
                            styleList
                        )
                    )
                    if (result) {
                        lifecycleScope.launch {

                            val job1 = async {
                                val newUser = userService.loadUserProfile(makeHeaderByAccessToken(it.accessToken))
                                activityViewModel.setUser(newUser.body()!!)
                            }
                            val job2 = async {
                                val newMyPage = userService.getMyPageInfo(makeHeaderByAccessToken(it.accessToken))
                                activityViewModel.setMyPageInfo(newMyPage)
                            }
                            val job3 = async {
                                val newHikingStyle = userService.getHikingStyle(makeHeaderByAccessToken(it.accessToken))
                                activityViewModel.setHikingStyles(newHikingStyle)
                            }
                            job1.await()
                            job2.await()
                            job3.await()
                            showToast("프로필 수정이 성공적으로 완료되었습니다!")
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                    }
                }
            }
        }
        loadTitle()
        binding.apply {
            Glide.with(root)
                .load(activityViewModel.user.userProfileImg)
                .into(ivProfileImg)
            etNickname.setText(activityViewModel.user.userNickName)

        }
        Log.d(TAG, "onViewCreated: ${activityViewModel.hikingStyles.value}")
        for (i in 1..HIKINGSTYLESIZE) {
            activityViewModel.hikingStyles.value?.let {
                if (it.contains(i)) {
                    hikeStyleList.add(HikingStyle(HIKINGSTYLE[i], true))
                } else {
                    hikeStyleList.add(HikingStyle(HIKINGSTYLE[i], false))
                }
            }
        }

        hikeAdapter = MyPageEditHikingStyleListAdapter()
        binding.rvHikeStyle.apply {
            itemAnimator = null
            adapter = hikeAdapter.apply {
                submitList(hikeStyleList)
                setItemClickListener(object : MyPageEditHikingStyleListAdapter.ItemClickListener {
                    override fun onClick(position: Int) {
                        hikeStyleList = hikeStyleList.mapIndexed { idx, it ->
                            if (idx == position) {
                                HikingStyle(it.style, !it.check)
                            } else {
                                HikingStyle(it.style, it.check)
                            }
                        }.toMutableList()
                        submitList(hikeStyleList)
                    }
                })
            }
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridSpaceItemDecoration(2, 60))
        }

        super.onViewCreated(view, savedInstanceState)

    }

    private fun loadTitle() {
        activityViewModel.token?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                titleList = userService.getMyBadgeList(makeHeaderByAccessToken(it.accessToken))
                launch(Dispatchers.Main) {
                    myPageAdapter =
                        ArrayAdapter(requireContext(), R.layout.item_search_spinner, titleList)
                    binding.spinnerTitle.adapter = myPageAdapter
                    binding.spinnerTitle.setSelection(titleList.indexOf(TITLE[activityViewModel.userTitle]))
                }
            }
        }
    }
}