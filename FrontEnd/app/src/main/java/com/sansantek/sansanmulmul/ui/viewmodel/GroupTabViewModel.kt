package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sansantek.sansanmulmul.data.model.Crew

class GroupTabViewModel : ViewModel() {
    private var _myCompletedList : MutableLiveData<List<Crew>> = MutableLiveData()
    val myCompletedList : LiveData<List<Crew>>
        get() = _myCompletedList

    fun setMyCompletedList(myList:List<Crew>){
        _myCompletedList.value = myList
    }


}