package com.sansantek.sansanmulmul.ui.view.grouptab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout.VERTICAL
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.Group
import com.sansantek.sansanmulmul.databinding.FragmentGroupTabBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupTabListAdapter
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.crewService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.view.groupdetail.GroupDetailFragment
import com.sansantek.sansanmulmul.ui.view.register.GroupCreateViewPagerFragment
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "GroupTabFragment 싸피"

class GroupTabFragment : BaseFragment<FragmentGroupTabBinding>(
    FragmentGroupTabBinding::bind,
    R.layout.fragment_group_tab
) {
    private val groupListInfoList = mutableListOf<Group>()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var groupTabListAdapter: GroupTabListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()

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

        // 그룹생성(플러스) 버튼 클릭시
        binding.btnFloating.setOnClickListener {
            val activity = requireActivity() as MainActivity
            activity.changeAddToBackstackFragment(GroupCreateViewPagerFragment())
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
                        loadMyScheduledGroupList()
                    }

                    1 -> {
                        binding.choiceInfo.text = "등산이 끝난 그룹만 보여드릴게요!"
                        loadMyCompletedGroupList()
                    }
                    //...
                    else -> {}
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
                    0 -> {}

                    1 -> {}
                    //...
                    else -> {}
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
                    0 -> {}

                    1 -> {}
                    //...
                    else -> {}
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
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
                    0 -> {}

                    1 -> {}
                    //...
                    else -> {}
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
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
                    0 -> {}

                    1 -> {}
                    //...
                    else -> {}
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {
        activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), false) }
        lifecycleScope.launch {
            activityViewModel.token?.let {
                val result = crewService.getMyScheduledCrew(makeHeaderByAccessToken(it.accessToken))
                if (result.isSuccessful) {
                    result.body()?.let { ret -> initRecyclerViewData(ret) }
                } else {
                    Log.d(TAG, "init: 그룹 서치 에러!")
                }
            }
        }
    }

    private fun loadMyCompletedGroupList() {
        activityViewModel.token?.let {
            lifecycleScope.launch {
                val result = crewService.getMyCompletedCrew(makeHeaderByAccessToken(it.accessToken))
                if (result.isSuccessful) {
                    result.body()?.let { list ->
                        groupTabListAdapter = initGroupListAdapter()
                        binding.groupList.adapter = groupTabListAdapter
                        groupTabListAdapter.submitList(list)
                        Log.d(TAG, "loadMyCompletedGroupList: 내 완료된 그룹 리스트! $list")
                    }
                } else {
                    Log.d(TAG, "loadMyCompletedGroupList: 나의 완료된 그룹 서치 실패!")
                }
            }
        }
    }

    private fun loadMyScheduledGroupList() {
        activityViewModel.token?.let {
            lifecycleScope.launch {
                val result = crewService.getMyScheduledCrew(makeHeaderByAccessToken(it.accessToken))
                if (result.isSuccessful) {
                    result.body()?.let { list ->
                        groupTabListAdapter = initGroupListAdapter()
                        binding.groupList.adapter = groupTabListAdapter
                        groupTabListAdapter.submitList(list)
                        Log.d(TAG, "loadMyScheduledGroupList: 내 계획된 그룹 리스트! $list")
                    }
                } else {
                    Log.d(TAG, "loadMyScheduledGroupList: 나의 완료된 그룹 서치 실패!")
                }
            }
        }
    }

    private fun initGroupListAdapter(): GroupTabListAdapter {
        return GroupTabListAdapter(isAllGroupLayout()).apply {
            setItemClickListener(object : GroupTabListAdapter.ItemClickListener {
                override fun onClick(crew: Crew) {
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    lifecycleScope.launch {
                        val result = isScheduleAlreadyExist(LocalDateTime.parse(crew.crewStartDate, formatter), LocalDateTime.parse(crew.crewEndDate, formatter))
                        if(result){
                            AlertRegisterGroupDialog().show(requireActivity().supportFragmentManager, "dialog")
                        }else{
                            ShowGroupPreviewDialog(crew).show(requireActivity().supportFragmentManager, "dialog")
                        }
                    }
                }

                override fun onGroupClick(position: Int) {
                    val activity = requireActivity() as MainActivity
                    activity.changeAddToBackstackFragment(GroupDetailFragment())
                }
            })
        }
    }

    private suspend fun isScheduleAlreadyExist(targetStartDate: LocalDateTime, targetEndDate: LocalDateTime): Boolean {
        activityViewModel.token?.let {
            val result = crewService.getMyScheduledCrew(makeHeaderByAccessToken(it.accessToken))
            if (result.isSuccessful) {
                result.body()?.let { myList ->
                    myList.forEach {
                        val myStartDate = parseDate(it.crewStartDate)
                        val myEndDate = parseDate(it.crewEndDate)

                        if ((myStartDate.isBefore(targetEndDate) && myStartDate.isAfter(targetStartDate)) || (myEndDate.isAfter(targetStartDate) && myEndDate.isBefore(targetEndDate))) {
                            return true
                        }
                    }
                }
            } else {
                Log.d(TAG, "isScheduleAlreadyExist: 내 그룹 목록 서치 에러!")
                return true
            }
        }
        return false
    }

    fun parseDate(dateString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return LocalDateTime.parse(dateString, formatter)
    }

    private fun isAllGroupLayout(): Boolean {
        return binding.customRadioBtn.checkedRadioButtonId == binding.radioAllBtn.id
    }

    private fun initRecyclerViewData(crewList: List<Crew>) {
        groupTabListAdapter = initGroupListAdapter()
        val decoration = DividerItemDecoration(requireContext(), VERTICAL)
        binding.groupList.addItemDecoration(decoration)
        binding.groupList.adapter = groupTabListAdapter
        groupTabListAdapter.submitList(crewList)
    }

    private fun radioButtonClickListener() {
        binding.customRadioBtn.setOnCheckedChangeListener { p0, p1 ->
            Log.d(TAG, "onViewCreated: ${p0.checkedRadioButtonId} $p1")
            if (p1 == binding.radioAllBtn.id) {
                binding.myGroupSpinner.visibility = View.GONE
                binding.allGroupTitle.visibility = View.VISIBLE
                lifecycleScope.launch {
                    activityViewModel.token?.let {
                        val result = crewService.getAllActivatedList(makeHeaderByAccessToken(it.accessToken))
                        binding.choiceInfo.text = "현재 일정이 진행 중인 그룹만 보여드릴게요!"
                        groupTabListAdapter = initGroupListAdapter()
                        if (result.isSuccessful) {
                            groupTabListAdapter.submitList(result.body())
                        }
                        binding.groupList.adapter = groupTabListAdapter
                    }
                }
            } else {
                binding.myGroupSpinner.visibility = View.VISIBLE
                binding.allGroupTitle.visibility = View.GONE

                groupTabListAdapter = initGroupListAdapter()

//          submitList(groupListInfoList)
            }
            binding.groupList.adapter = groupTabListAdapter

            if (binding.myGroupSpinner.selectedItem == "진행 중인 그룹") {
                binding.choiceInfo.text = "현재 등산이 진행 중이거나 예정인 그룹만 보여드릴게요!"
            } else {
                binding.choiceInfo.text = "등산이 끝난 그룹만 보여드릴게요!"
            }
        }
    }
}
