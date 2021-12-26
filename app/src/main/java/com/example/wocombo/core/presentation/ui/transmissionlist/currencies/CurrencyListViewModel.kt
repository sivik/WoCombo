package com.example.wocombo.core.presentation.ui.transmissionlist.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wocombo.common.functional.SingleLiveData
import com.example.wocombo.core.domain.usecases.DownloadCurrenciesUseCase
import com.example.wocombo.core.presentation.enums.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyListViewModel(
    private val downloadCurrenciesUseCase: DownloadCurrenciesUseCase
) : ViewModel() {

    private val _currencyLiveData: SingleLiveData<DownloadCurrenciesUseCase.Result> by lazy { SingleLiveData() }
    val currencyLiveData: LiveData<DownloadCurrenciesUseCase.Result>
        get() = _currencyLiveData

    fun downloadCurrencies(sortType: SortType) {
        viewModelScope.launch(Dispatchers.Default) {
            val request = DownloadCurrenciesUseCase.Request(sortType)
            val result = downloadCurrenciesUseCase.execute(request)
            _currencyLiveData.postValue(result)
        }
    }
}