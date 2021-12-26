package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.UseCase
import com.example.wocombo.core.data.exceptions.DownloadCurrencyException
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.CurrencyFailures
import com.example.wocombo.core.domain.models.Currency
import com.example.wocombo.core.domain.repositories.CurrencyRepository
import com.example.wocombo.core.presentation.enums.SortType
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

class DownloadCurrenciesUseCase(
    private val repository: CurrencyRepository
) : UseCase<DownloadCurrenciesUseCase.Request, DownloadCurrenciesUseCase.Result> {

    data class Request(
        val sortType: SortType
    ) : UseCase.UseCaseRequest

    data class Result(
        val currencies: List<Currency>? = null,
        val failure: Failure? = null
    ) : UseCase.UseCaseResponse

    override fun execute(request: Request): Result = try {
        val currencies = repository.getCurrencies()
        val sortedList = when (request.sortType) {
            SortType.ASCENDING -> currencies.sortedBy { it.name }
            SortType.DESCENDING -> currencies.sortedByDescending { it.name }
            SortType.ASCENDING_VOLUME -> currencies.sortedBy { it.volume }
            SortType.DESCENDING_VOLUME -> currencies.sortedByDescending { it.volume }
            SortType.ASCENDING_PERCENT -> currencies.sortedBy { it.percentDay }
            SortType.DESCENDING_PERCENT -> currencies.sortedByDescending { it.percentDay }
        }
        Result(currencies = sortedList)

    } catch (e: UnknownHostException) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = CommunicationsFailures.NoInternetFailure)
    } catch (e: TimeoutException) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = CommunicationsFailures.ConnectionFailure)
    } catch (e: InterruptedIOException) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = CommunicationsFailures.ConnectionFailure)
    } catch (e: SSLException) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = CommunicationsFailures.InternalServerFailure)
    } catch (e: ConnectException) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = CommunicationsFailures.NoInternetFailure)
    } catch (e: DownloadCurrencyException) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = CurrencyFailures.DownloadCurrenciesFailure)
    } catch (e: Exception) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = Failure.UnknownFailure)
    }
}