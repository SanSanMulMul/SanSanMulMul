package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.min

class CreateGroupViewModel : ViewModel() {
    private var _groupTitle: String = ""
    val groupTitle: String
        get() = _groupTitle
    fun setGroupTitle(title: String){
        _groupTitle = title
    }

    private var _groupDescription: String = ""
    val groupDescription: String
        get() = _groupDescription
    fun setGroupDescription(description: String){
        _groupDescription = description
    }

    private var _maxMember: Int = -1
    val maxMember: Int
        get() = _maxMember
    fun setMaxMember(maxMember: Int){
        _maxMember = min(maxMember, 10)
    }

    private var _crewStyle: MutableLiveData<List<Int>> = MutableLiveData(mutableListOf())
    val crewStyle: MutableLiveData<List<Int>>
        get() = _crewStyle
    fun setCrewStyle(newStyle: List<Int>){
        _crewStyle.value = newStyle
    }

    private var _groupGender: String = "A"
    val groupGender: String
        get() = _groupGender
    fun setGroupGender(groupGender: String){
        _groupGender = groupGender
    }

    private var _groupMinAge: Int = -1
    val groupMinAge: Int
        get() = _groupMinAge
    fun setGroupMinAge(groupMinAge: Int){
        _groupMinAge = groupMinAge
    }

    private var _groupMaxAge: Int = -1
    val groupMaxAge: Int
        get() = _groupMaxAge
    fun setGroupMaxAge(groupMaxAge: Int){
        _groupMaxAge = groupMaxAge
    }

    private var _groupStartDate: String = ""
    val groupStartDate: String
        get() = _groupStartDate
    fun setGroupStartDate(groupStartDate: String){
        _groupStartDate = groupStartDate
    }

    private var _groupEndDate: String = ""
    val groupEndDate: String
        get() = _groupEndDate
    fun setGroupEndDate(groupEndDate: String){
        _groupEndDate = groupEndDate
    }

    private var _groupHikingStyleList: MutableList<Int> = mutableListOf()
    val groupHikingStyleList : List<Int>
        get() = _groupHikingStyleList
    fun setGroupHikingStyleList(groupHikingStyleList: MutableList<Int>){
        _groupHikingStyleList = groupHikingStyleList
    }

    private var _groupMountainId: Int = -1
    val groupMountainId: Int
        get() = _groupMountainId
    fun setGroupMountainId(groupMountainId: Int){
        _groupMountainId = groupMountainId
    }

    private var _groupUpCourseId: MutableLiveData<Long> = MutableLiveData(-1)
    val groupUpCourseId: MutableLiveData<Long>
        get() = _groupUpCourseId
    fun setGroupUpCourseId(groupUpCourseId: Long){
        _groupUpCourseId.value = groupUpCourseId
    }

    private var _groupDownCourseId: MutableLiveData<Long> = MutableLiveData(-1)
    val groupDownCourseId: MutableLiveData<Long>
        get() = _groupDownCourseId
    fun setGroupDownCourseId(groupDownCourseId: Long){
        _groupDownCourseId.value = groupDownCourseId
    }

    private var _groupUpCourseName: String = ""
    val groupUpCourseName: String
        get() = _groupUpCourseName
    fun setGroupUpCourseName(groupUpCourseName: String){
        _groupUpCourseName = groupUpCourseName
    }

    private var _groupDownCourseName: String = ""
    val groupDownCourseName: String
        get() = _groupDownCourseName
    fun setGroupDownCourseName(groupDownCourseName: String){
        _groupDownCourseName = groupDownCourseName
    }

    private var _startDate: MutableLiveData<String> = MutableLiveData("")
    val startDate: MutableLiveData<String>
        get() = _startDate
    fun setStartDate(startDate: String){
        _startDate.value = startDate
    }

    private var _endDate: MutableLiveData<String> = MutableLiveData("")
    val endDate: MutableLiveData<String>
        get() = _endDate
    fun setEndDate(endDate: String){
        _endDate.value = endDate
    }
}