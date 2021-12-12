package com.example.wocombo.core.presentation.ui.transmissionlist.schedules

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wocombo.common.functional.SingleLiveData
import com.example.wocombo.core.domain.usecases.DownloadSchedulesUseCase
import com.example.wocombo.core.presentation.enums.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleListViewModel(
    private val downloadSchedulesUseCase: DownloadSchedulesUseCase
) : ViewModel() {

    private val _scheduleLiveData: SingleLiveData<DownloadSchedulesUseCase.Result> by lazy { SingleLiveData() }
    val scheduleLiveData: LiveData<DownloadSchedulesUseCase.Result>
        get() = _scheduleLiveData

    fun downloadSchedules(sortType: SortType) {
        viewModelScope.launch(Dispatchers.Default) {
            val request = DownloadSchedulesUseCase.Request(sortType)
            val result = downloadSchedulesUseCase.execute(request)
            _scheduleLiveData.postValue(result)
        }
    }
}