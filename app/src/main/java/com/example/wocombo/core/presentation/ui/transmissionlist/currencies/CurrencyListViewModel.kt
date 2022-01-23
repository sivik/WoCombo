package com.example.wocombo.core.presentation.ui.transmissionlist.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wocombo.common.functional.SingleLiveData
import com.example.wocombo.core.domain.usecases.DownloadCurrenciesUseCase
import com.example.wocombo.core.domain.usecases.LoginUseCase
import com.example.wocombo.core.presentation.enums.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CurrencyListViewModel(
    private val downloadCurrenciesUseCase: DownloadCurrenciesUseCase
) : ViewModel() {

    private val _currencies: SingleLiveData<DownloadCurrenciesUseCase.Result> by lazy { SingleLiveData() }
    val currencies: LiveData<DownloadCurrenciesUseCase.Result>
        get() = _currencies

    fun downloadCurrencies(sortType: SortType) {

        viewModelScope.launch(Dispatchers.Default) {
            flow {
                val request = DownloadCurrenciesUseCase.Request(sortType)
                emit(downloadCurrenciesUseCase.execute(request))
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = DownloadCurrenciesUseCase.Result()
            ).collect {
                _currencies.value = it
            }
        }
    }
}