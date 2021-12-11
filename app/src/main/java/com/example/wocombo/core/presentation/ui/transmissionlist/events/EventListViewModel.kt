package com.example.wocombo.core.presentation.ui.transmissionlist.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wocombo.common.functional.SingleLiveData
import com.example.wocombo.core.domain.usecases.DownloadEventsUseCase
import com.example.wocombo.core.domain.usecases.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventListViewModel(
    private val downloadEventsUseCase: DownloadEventsUseCase
) : ViewModel() {

    private val _eventsLiveData: SingleLiveData<DownloadEventsUseCase.Result> by lazy { SingleLiveData() }
    val eventsLiveData: LiveData<DownloadEventsUseCase.Result>
    get() = _eventsLiveData

    fun downloadEvents() {
        viewModelScope.launch(Dispatchers.Default) {
            val request = DownloadEventsUseCase.Request()
            val result = downloadEventsUseCase.execute(request)
            _eventsLiveData.postValue(result)
        }
    }
}