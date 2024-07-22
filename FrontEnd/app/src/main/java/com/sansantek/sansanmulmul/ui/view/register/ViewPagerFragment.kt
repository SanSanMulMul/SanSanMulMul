package com.sansantek.sansanmulmul.ui.view.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sansantek.sansanmulmul.databinding.FragmentViewPagerBinding
import com.sansantek.sansanmulmul.ui.viewpageradapter.ViewPageAdapter

private const val TAG = "ViewPagerFragment 싸피"
class ViewPagerFragment : Fragment() {
    private val binding by lazy{ FragmentViewPagerBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val adapter = ViewPageAdapter(requireActivity())
        binding.viewPager.adapter =  adapter
        binding.viewPager.isUserInputEnabled = false
        binding.springDotsIndicator.attachTo(binding.viewPager)

        checkViewPageLimit()

        binding.nextButton.setOnClickListener{
            binding.viewPager.currentItem += 1
            checkViewPageLimit()
        }
        binding.prevButton.setOnClickListener{
            binding.viewPager.currentItem -= 1
            checkViewPageLimit()
        }
        return binding.root

    }
    fun checkViewPageLimit(){
        val itemCount = binding.viewPager.adapter?.itemCount ?: 0
        Log.d(TAG, "checkViewPageLimit: ${binding.viewPager.childCount}")
        if(binding.viewPager.currentItem == itemCount-1){
            binding.nextButton.visibility = View.INVISIBLE
            binding.homeButton.visibility = View.VISIBLE

        }
        else{
            binding.nextButton.visibility = View.VISIBLE
            binding.homeButton.visibility = View.INVISIBLE
        }
        if(binding.viewPager.currentItem == 0){
            binding.prevButton.visibility = View.INVISIBLE
        }
        else{
            binding.prevButton.visibility = View.VISIBLE
        }
    }

}