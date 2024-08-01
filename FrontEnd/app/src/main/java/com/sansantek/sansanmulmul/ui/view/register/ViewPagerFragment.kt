package com.sansantek.sansanmulmul.ui.view.register

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
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

        val adapter = LoginViewPagerAdapter(this)
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

    fun changeNextButtonVisibility(visibility: Int){
        binding.nextButton.visibility = visibility
    }
    fun enableNextButton(enable: Boolean){
        if(enable){
            binding.nextButton.imageTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            binding.nextButton.background.setTintList(ContextCompat.getColorStateList(requireContext(), R.color.green))
        }
        else{
            binding.nextButton.imageTintList = ColorStateList.valueOf(Color.parseColor("#FF373737"))
            binding.nextButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.gray_border_rounded_box)
        }
        binding.nextButton.isEnabled = enable
    }

    fun changePrevButtonVisibility(visibility: Int){
        binding.prevButton.visibility = visibility
    }
    fun enablePrevButton(enable: Boolean){
        binding.prevButton.isEnabled = enable
    }

}