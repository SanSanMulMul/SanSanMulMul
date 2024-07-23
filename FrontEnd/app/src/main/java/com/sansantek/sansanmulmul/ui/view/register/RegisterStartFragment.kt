package com.sansantek.sansanmulmul.ui.view.register

import android.os.Bundle
import android.view.View
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentRegisterStartBinding

class RegisterStartFragment : BaseFragment<FragmentRegisterStartBinding>(
    FragmentRegisterStartBinding::bind,
    R.layout.fragment_register_start
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_view, ViewPagerFragment())
                .addToBackStack(null).commit()
        }

    }
}