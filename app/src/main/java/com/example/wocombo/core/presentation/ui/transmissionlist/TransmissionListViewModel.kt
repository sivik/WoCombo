package com.example.wocombo.core.presentation.ui.transmissionlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wocombo.core.presentation.enums.SortType

class TransmissionListViewModel : ViewModel() {

    private val _sortLiveData: MutableLiveData<SortType> by lazy { MutableLiveData<SortType>() }

    val sortLiveData: LiveData<SortType>
        get() = _sortLiveData

    fun setSortType(type: SortType) {
        _sortLiveData.postValue(type)
    }

}