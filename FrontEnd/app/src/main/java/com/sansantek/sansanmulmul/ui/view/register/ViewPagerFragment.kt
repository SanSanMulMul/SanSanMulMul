package com.sansantek.sansanmulmul.ui.view.register

import android.os.Bundle
import android.util.Log
import android.view.View
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentLoginViewPagerBinding
import com.sansantek.sansanmulmul.ui.adapter.LoginViewPagerAdapter


private const val TAG = "ViewPagerFragment 싸피"

class ViewPagerFragment : BaseFragment<FragmentLoginViewPagerBinding>(
    FragmentLoginViewPagerBinding::bind,
    R.layout.fragment_login_view_pager
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LoginViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        binding.springDotsIndicator.attachTo(binding.viewPager)

        checkViewPageLimit()

        binding.nextButton.setOnClickListener {
            binding.viewPager.currentItem += 1
            checkViewPageLimit()
        }
        binding.prevButton.setOnClickListener {
            binding.viewPager.currentItem -= 1
            checkViewPageLimit()
        }


    }

    private fun checkViewPageLimit() {
        val itemCount = binding.viewPager.adapter?.itemCount ?: 0
        Log.d(TAG, "checkViewPageLimit: ${binding.viewPager.childCount}")
        if (binding.viewPager.currentItem == itemCount - 1) {
            binding.nextButton.visibility = View.GONE
            binding.prevButton.visibility = View.GONE
            binding.progressButton.visibility = View.GONE
        } else if (binding.viewPager.currentItem == 0) {
            binding.nextButton.visibility = View.VISIBLE
            binding.prevButton.visibility = View.INVISIBLE
        } else {
            binding.nextButton.visibility = View.VISIBLE
            binding.prevButton.visibility = View.VISIBLE
        }
    }

}