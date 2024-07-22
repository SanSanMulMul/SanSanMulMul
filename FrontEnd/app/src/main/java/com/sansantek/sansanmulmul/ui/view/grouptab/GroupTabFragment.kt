package com.sansantek.sansanmulmul.ui.view.grouptab

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getColor
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupTabBinding

class GroupTabFragment : BaseFragment<FragmentGroupTabBinding>(FragmentGroupTabBinding::bind, R.layout.fragment_group_tab) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.customRadioBtn.setTintColor(getColor(requireActivity(), R.color.green))
        val items = resources.getStringArray(R.array.grouptab_search_spinner_menu_array)
        val myAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)

        binding.spinner.adapter = myAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when (position) {
                    0 -> {

                    }

                    1 -> {

                    }
                    //...
                    else -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}