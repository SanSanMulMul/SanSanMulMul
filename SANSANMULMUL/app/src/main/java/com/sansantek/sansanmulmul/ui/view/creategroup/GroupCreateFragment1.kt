package com.sansantek.sansanmulmul.ui.view.creategroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupCreate1Binding

class GroupCreateFragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentGroupCreate1Binding.inflate(layoutInflater)

        return binding.root
    }
}