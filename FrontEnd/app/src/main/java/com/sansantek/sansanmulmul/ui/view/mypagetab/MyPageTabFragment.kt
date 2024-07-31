package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMyPageTabBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.MyPageHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.layoutmanager.CustomLayoutmanager
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.view.groupdetail.GroupDetailFragment

private const val TAG = "MyPageTabFragment_싸피"
class MyPageTabFragment : BaseFragment<FragmentMyPageTabBinding>(
    FragmentMyPageTabBinding::bind,
    R.layout.fragment_my_page_tab
) {
    private val styleList = mutableListOf("#등산도 식후경", "#등산은 사진이지", "#설렁설렁", "#저쩌구")
    private lateinit var myPageHikingStyleListAdapter: MyPageHikingStyleListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val parentFragment = parentFragment as GroupDetailFragment
                    parentFragment.popBackStackGroupDetailFragmentView()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myPageHikingStyleListAdapter = MyPageHikingStyleListAdapter()
        binding.rvMyHikingStyle.apply {
            adapter = myPageHikingStyleListAdapter.apply {
                submitList(styleList)
            }
            layoutManager = CustomLayoutmanager(requireContext(), 2)
        }

        replaceFragment(MyPageFirstTabFragment())

        binding.btnEditProfile.setOnClickListener{
            val activity = requireActivity() as MainActivity
            activity.changeAddToBackstackFragment(MyPageEditTabFragment())
        }

        binding.tlTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: ${tab?.position}")
                when(tab?.position){
                    0 -> {
                        replaceFragment(MyPageFirstTabFragment())
                    }
                    else -> {
                        replaceFragment(MyPageSecondTabFragment())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        super.onViewCreated(view, savedInstanceState)
    }

    fun replaceFragment(view: Fragment){
        childFragmentManager.beginTransaction().replace(binding.myPageFragmentView.id, view).commit()
    }
}