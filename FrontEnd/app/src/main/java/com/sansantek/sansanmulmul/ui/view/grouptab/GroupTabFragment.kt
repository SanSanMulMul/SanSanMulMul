package com.sansantek.sansanmulmul.ui.view.grouptab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout.VERTICAL
import androidx.recyclerview.widget.DividerItemDecoration
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Group
import com.sansantek.sansanmulmul.databinding.FragmentGroupTabBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupTabListAdapter
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.view.creategroup.GroupIntroduceCreateFragment
import com.sansantek.sansanmulmul.ui.view.groupdetail.GroupDetailFragment

private const val TAG = "GroupTabFragment 싸피"
class GroupTabFragment : BaseFragment<FragmentGroupTabBinding>(
    FragmentGroupTabBinding::bind,
    R.layout.fragment_group_tab
) {
    private val groupListInfoList = mutableListOf<Group>()
    private lateinit var groupTabListAdapter: GroupTabListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchItems = resources.getStringArray(R.array.grouptab_search_spinner_menu_array)
        val ageItems = resources.getStringArray(R.array.grouptab_age_spinner_menu_array)
        val styleItems = resources.getStringArray(R.array.grouptab_style_spinner_menu_array)
        val genderItems = resources.getStringArray(R.array.grouptab_gender_spinner_menu_array)
        val groupItems = resources.getStringArray(R.array.grouptab_group_spinner_menu_array)

        val searchAdapter =
            ArrayAdapter(requireContext(), R.layout.item_search_spinner, searchItems)
        val ageAdapter = ArrayAdapter(requireContext(), R.layout.item_filter_spinner, ageItems)
        val styleAdapter = ArrayAdapter(requireContext(), R.layout.item_filter_spinner, styleItems)
        val genderAdapter =
            ArrayAdapter(requireContext(), R.layout.item_filter_spinner, genderItems)

        val groupAdapter = ArrayAdapter(requireContext(), R.layout.item_group_spinner, groupItems)

        binding.searchSpinner.adapter = searchAdapter

        binding.ageSpinner.adapter = ageAdapter
        binding.styleSpinner.adapter = styleAdapter
        binding.genderSpinner.adapter = genderAdapter
        binding.myGroupSpinner.adapter = groupAdapter

        radioButtonClickListener()
        initRecyclerViewData()

        binding.btnFloating.setOnClickListener{
            val activity = requireActivity() as MainActivity
            activity.changeAddToBackstackFragment(GroupIntroduceCreateFragment())
        }

        binding.myGroupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, "myGroupSpinner: myGroupSpinner item selected Listener 작동")
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when (position) {
                    0 -> {
                        binding.choiceInfo.text = "현재 등산이 진행 중이거나 예정인 그룹만 보여드릴게요!"
                    }

                    1 -> {
                        binding.choiceInfo.text = "등산이 끝난 그룹만 보여드릴게요!"
                    }
                    //...
                    else -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.ageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
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

        binding.searchSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, "myGroupSpinner: myGroupSpinner item selected Listener 작동")
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
        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
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
        binding.styleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
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

    private fun isAllGroupLayout(): Boolean{
        return binding.customRadioBtn.checkedRadioButtonId == binding.radioAllBtn.id
    }

    private fun initRecyclerViewData(){
        for (i in 1..15) {
            groupListInfoList.add(Group("가야산을 사랑하는 한사랑 산악회의 소모임입니다"))
        }

        groupTabListAdapter = GroupTabListAdapter(isAllGroupLayout()).apply {
            setItemClickListener(object : GroupTabListAdapter.ItemClickListener{
                override fun onClick(position: Int) {
                    AlertRegisterGroupDialog().show(requireActivity().supportFragmentManager, "dialog")
                }

                override fun onGroupClick(position: Int) {
                    val activity = requireActivity() as MainActivity
                    activity.changeAddToBackstackFragment(GroupDetailFragment())
                }
            })
        }

        binding.groupList.adapter = groupTabListAdapter
        groupTabListAdapter.submitList(groupListInfoList)

        val decoration = DividerItemDecoration(requireContext(), VERTICAL)
        binding.groupList.addItemDecoration(decoration)
    }

    private fun radioButtonClickListener(){
        binding.customRadioBtn.setOnCheckedChangeListener { p0, p1 ->
            Log.d(TAG, "onViewCreated: ${p0.checkedRadioButtonId} $p1")
            if (p1 == binding.radioAllBtn.id){
                binding.myGroupSpinner.visibility = View.GONE
                binding.allGroupTitle.visibility = View.VISIBLE
                binding.choiceInfo.text = "현재 일정이 진행 중인 그룹만 보여드릴게요!"
                groupTabListAdapter = GroupTabListAdapter(isAllGroupLayout()).apply {
                    setItemClickListener(object : GroupTabListAdapter.ItemClickListener{
                        override fun onClick(position: Int) {
                            AlertRegisterGroupDialog().show(requireActivity().supportFragmentManager, "dialog")
                        }

                        override fun onGroupClick(position: Int) {
                        }
                    })
                    submitList(groupListInfoList)
                }
                binding.groupList.adapter = groupTabListAdapter
            }
            else{
                binding.myGroupSpinner.visibility = View.VISIBLE
                binding.allGroupTitle.visibility = View.GONE

                groupTabListAdapter = GroupTabListAdapter(isAllGroupLayout()).apply {

                    setItemClickListener(object : GroupTabListAdapter.ItemClickListener{
                        override fun onClick(position: Int) {
                        }

                        override fun onGroupClick(position: Int) {
                            val activity = requireActivity() as MainActivity
                            activity.changeAddToBackstackFragment(GroupDetailFragment())
                        }
                    })

                    submitList(groupListInfoList)
                }
                binding.groupList.adapter = groupTabListAdapter

                if(binding.myGroupSpinner.selectedItem == "진행 중인 그룹"){
                    binding.choiceInfo.text = "현재 등산이 진행 중이거나 예정인 그룹만 보여드릴게요!"
                }
                else{
                    binding.choiceInfo.text = "등산이 끝난 그룹만 보여드릴게요!"
                }
            }
        }
    }
}