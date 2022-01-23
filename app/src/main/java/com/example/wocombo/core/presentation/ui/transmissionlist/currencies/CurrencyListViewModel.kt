package com.example.wocombo.core.presentation.ui.transmissionlist.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wocombo.core.domain.usecases.DownloadCurrenciesFlowUseCase
import com.example.wocombo.core.presentation.enums.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.wocombo.common.functional.Result
class CurrencyListViewModel(
    private val flowUseCase: DownloadCurrenciesFlowUseCase
) : ViewModel() {

    private val _currencies: MutableStateFlow<Result<DownloadCurrenciesFlowUseCase.Response>?> =
        MutableStateFlow(null)
    val currencies: StateFlow<Result<DownloadCurrenciesFlowUseCase.Response>?> = _currencies

    fun downloadCurrencies(sortType: SortType) {
        viewModelScope.launch(Dispatchers.Default) {
            flowUseCase.invoke(DownloadCurrenciesFlowUseCase.Request(sortType))
                .collect {
                    _currencies.value = it
                }
        }
    }



    /*Douczyć się stateIn i shareIn, ktore zmieniaja cold na hot*/

    //flow {
    //    val request = DownloadCurrenciesUseCase.Request(sortType)
    //    emit(downloadCurrenciesUseCase.execute(request))
    //}.stateIn(
    //scope = viewModelScope,
    //started = SharingStarted.Lazily,
    //initialValue = DownloadCurrenciesUseCase.Result()
    //).collect {
    //    _currencies.value = it
    //}
}