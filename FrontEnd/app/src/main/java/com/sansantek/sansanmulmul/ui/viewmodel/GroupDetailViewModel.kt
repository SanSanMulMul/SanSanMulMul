package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sansantek.sansanmulmul.data.model.CrewGallery

class GroupDetailViewModel: ViewModel() {
    private var _pictureList: MutableLiveData<List<CrewGallery>> = MutableLiveData()
    val pictureList : MutableLiveData<List<CrewGallery>>
        get() = _pictureList
    fun setPictureList(pictureList: List<CrewGallery>){
        _pictureList.value = pictureList
    }
}