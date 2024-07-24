package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.content.Context
import android.drm.DrmStore.ConstraintsColumns
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailBinding
import com.sansantek.sansanmulmul.databinding.PopupGroupDetailNotiBinding

private const val TAG = "GroupTabFragment 싸피"
class GroupDetailFragment : BaseFragment<FragmentGroupDetailBinding>(
    FragmentGroupDetailBinding::bind,
    R.layout.fragment_group_detail
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().supportFragmentManager.beginTransaction().replace(binding.groupDetailTabFragmentView.id, GroupDetailTabFirstInfoFragment()).commit()
        binding.layoutTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: $tab")
                requireActivity().supportFragmentManager.beginTransaction().replace(binding.groupDetailTabFragmentView.id, GroupDetailTabFirstInfoFragment()).commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.ibNoti.setOnClickListener {
            Log.d(TAG, "onViewCreated: 클릭 눌렀는데")
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenWidth = getScreenWidth(requireContext())
            val screenHeight = getScreenHeight(requireContext())

            Log.d(TAG, "onViewCreated: $screenHeight , $screenWidth")
            val layoutInflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val bbinding = PopupGroupDetailNotiBinding.inflate(layoutInflater)
            val popup = PopupWindow(requireContext()).apply {
                contentView = bbinding.root
                val newWidth = (screenWidth * 0.8).toInt()
                val newHeight = (screenHeight * 0.8).toInt()
                width = newWidth
                height = newHeight
                animationStyle = R.style.popup_window_animation

            }
            bbinding.closeBtn.setOnClickListener{
                popup.dismiss()
            }

//            val binding = PopupLayoutBinding.inflate(layoutInflater)
//            val popUp = PopupWindow(context)
//            popUp.contentView = binding.root
//            popUp.width = LinearLayout.LayoutParams.WRAP_CONTENT
//            popUp.height = LinearLayout.LayoutParams.WRAP_CONTENT
//            popUp.isFocusable = true
//
//            val x = 200
//            val y = 60
//            popUp.setBackgroundDrawable(ColorDrawable())
//            popUp.animationStyle = R.style.popup_window_animation
//            popUp.showAtLocation(binding.root, Gravity.NO_GRAVITY, p.x + x, p.y + y)

//

//            val newWidth = (screenWidth * 0.8).toInt()
//            val newHeight = (screenHeight * 0.8).toInt()
//            val popuplayoutParams = requireView().layoutParams
//            Log.d(TAG, "onViewCreated: $popuplayoutParams")
//            popuplayoutParams.width = newWidth
//            popuplayoutParams.height = newHeight
//            popup.contentView.layoutParams = popuplayoutParams
            popup.showAsDropDown(binding.layoutChatBtn)

        }
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
}