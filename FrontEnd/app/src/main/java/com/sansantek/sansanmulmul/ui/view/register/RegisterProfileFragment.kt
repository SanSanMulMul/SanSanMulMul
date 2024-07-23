package com.sansantek.sansanmulmul.ui.view.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sansantek.sansanmulmul.databinding.FragmentRegisterProfileBinding
import com.sansantek.sansanmulmul.ui.view.grouptab.ShowGroupPreviewDialog

class RegisterProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentRegisterProfileBinding.inflate(layoutInflater)
        ShowGroupPreviewDialog().show(requireActivity().supportFragmentManager, "dialog")


        return binding.root
    }
}