package com.sansantek.sansanmulmul.ui.view.creategroup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.databinding.FragmentGroupExtraInfoBinding


class GroupExtraInfoFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentGroupExtraInfoBinding.inflate(layoutInflater)
        return binding.root
    }
}