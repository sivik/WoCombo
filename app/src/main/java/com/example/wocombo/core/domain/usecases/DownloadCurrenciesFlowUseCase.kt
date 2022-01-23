package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.FlowUseCase
import com.example.wocombo.core.domain.models.Currency
import com.example.wocombo.core.domain.repositories.CurrencyRepository
import com.example.wocombo.core.presentation.enums.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.wocombo.common.functional.Result
import com.example.wocombo.core.data.exceptions.DownloadCurrencyException
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.CurrencyFailures
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException


class DownloadCurrenciesFlowUseCase(
    private val repository: CurrencyRepository
) : FlowUseCase<DownloadCurrenciesFlowUseCase.Response, DownloadCurrenciesFlowUseCase.Request>() {

    data class Request(
        val sortType: SortType
    ) : FlowRequest

    data class Response(
        val currencies: List<Currency>? = null,
        val fakeInfo: String = ""
    ) : FlowResponse


    override fun invoke(request: Request): Flow<Result<Response>> = flow {
        try {
            emit(Result.Loading())
            val currencies = repository.getCurrencies()
            val sortedList = when (request.sortType) {
                SortType.ASCENDING -> currencies.sortedBy { it.name }
                SortType.DESCENDING -> currencies.sortedByDescending { it.name }
                SortType.ASCENDING_VOLUME -> currencies.sortedBy { it.volume }
                SortType.DESCENDING_VOLUME -> currencies.sortedByDescending { it.volume }
                SortType.ASCENDING_PERCENT -> currencies.sortedBy { it.percentDay }
                SortType.DESCENDING_PERCENT -> currencies.sortedByDescending { it.percentDay }
            }
            emit(Result.Success(Response(sortedList)))
        }catch (e: UnknownHostException) {
            logUseCaseError(e, javaClass.simpleName)
            emit(Result.Error(failure = CommunicationsFailures.NoInternetFailure))
        } catch (e: TimeoutException) {
            logUseCaseError(e, javaClass.simpleName)
            DownloadCurrenciesUseCase.Result(failure = CommunicationsFailures.ConnectionFailure)
        } catch (e: InterruptedIOException) {
            logUseCaseError(e, javaClass.simpleName)
            DownloadCurrenciesUseCase.Result(failure = CommunicationsFailures.ConnectionFailure)
        } catch (e: SSLException) {
            logUseCaseError(e, javaClass.simpleName)
            DownloadCurrenciesUseCase.Result(failure = CommunicationsFailures.InternalServerFailure)
        } catch (e: ConnectException) {
            logUseCaseError(e, javaClass.simpleName)
            DownloadCurrenciesUseCase.Result(failure = CommunicationsFailures.NoInternetFailure)
        } catch (e: DownloadCurrencyException) {
            logUseCaseError(e, javaClass.simpleName)
            DownloadCurrenciesUseCase.Result(failure = CurrencyFailures.DownloadCurrenciesFailure)
        } catch (e: Exception) {
            logUseCaseError(e, javaClass.simpleName)
            DownloadCurrenciesUseCase.Result(failure = Failure.UnknownFailure)
        }
    }
}