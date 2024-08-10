package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sansantek.sansanmulmul.data.model.Crew
import org.apache.commons.lang3.mutable.Mutable


class GroupTabViewModel : ViewModel() {
    private var _myCompletedList : MutableLiveData<List<Crew>> = MutableLiveData()
    val myCompletedList : LiveData<List<Crew>>
        get() = _myCompletedList

    fun setMyCompletedList(myList:List<Crew>){
        _myCompletedList.value = myList
    }

    private var _myScheduledList : MutableLiveData<List<Crew>> = MutableLiveData()
    val myScheduledList : LiveData<List<Crew>>
        get() = _myScheduledList

    fun setMyScheduledList(myList:List<Crew>){
        _myScheduledList.value = myList
    }

    private var _allList : MutableLiveData<List<Crew>> = MutableLiveData()
    val allList : LiveData<List<Crew>>
        get() = _allList

    fun setAllList(allList:List<Crew>){
        _allList.value = allList
    }

    private var _selected : MutableLiveData<Int> = MutableLiveData(1)
    val selected: MutableLiveData<Int>
        get() = _selected

    fun setSelected(select: Int){
        _selected.value = select
    }
}