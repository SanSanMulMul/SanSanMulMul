package com.sansantek.sansanmulmul.ui.view.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.databinding.FragmentRegisterStartBinding

class RegisterStartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentRegisterStartBinding.inflate(layoutInflater)

        binding.button.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_view, ViewPagerFragment())
                .addToBackStack(null).commit()
        }

        return binding.root
    }
}