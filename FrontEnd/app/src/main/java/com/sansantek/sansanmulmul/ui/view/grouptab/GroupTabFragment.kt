package com.sansantek.sansanmulmul.ui.view.grouptab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupTabBinding

class GroupTabFragment : BaseFragment<FragmentGroupTabBinding>(FragmentGroupTabBinding::bind, R.layout.fragment_group_tab) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }
}