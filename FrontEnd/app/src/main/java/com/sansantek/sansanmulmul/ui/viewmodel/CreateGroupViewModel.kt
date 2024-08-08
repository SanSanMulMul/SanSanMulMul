package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.ViewModel

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
        _groupTitle = description
    }

    private var _maxMember: Int = -1
    val maxMember: Int
        get() = _maxMember
    fun setMaxMember(maxMember: Int){
        _maxMember = maxMember
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

    private var _groupUpCourseId: Long = -1
    val groupUpCourseId: Long
        get() = _groupUpCourseId
    fun setGroupUpCourseId(groupUpCourseId: Long){
        _groupUpCourseId = groupUpCourseId
    }

    private var _groupDownCourseId: Long = -1
    val groupDownCourseId: Long
        get() = _groupDownCourseId
    fun setGroupDownCourseId(groupDownCourseId: Long){
        _groupDownCourseId = groupDownCourseId
    }
}