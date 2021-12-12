package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.UseCase
import com.example.wocombo.core.data.exceptions.DownloadEventsException
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.EventFailures
import com.example.wocombo.core.domain.models.Event
import com.example.wocombo.core.domain.repositories.EventsRepository
import com.example.wocombo.core.presentation.enums.SortType
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

class DownloadEventsUseCase(
    private val repository: EventsRepository
) : UseCase<DownloadEventsUseCase.Request, DownloadEventsUseCase.Result> {

    data class Request(
        val sortType: SortType
    ) : UseCase.UseCaseRequest

    data class Result(
        val events: List<Event>? = null,
        val failure: Failure? = null
    ) : UseCase.UseCaseResponse

    override fun execute(request: Request): Result = try {
        val events = repository.downloadEvents()
        val sortedList = when (request.sortType) {
            SortType.ASCENDING -> events.sortedBy { it.date.millis }
            SortType.DESCENDING -> events.sortedByDescending { it.date.millis }
        }
        Result(events = sortedList)
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
    } catch (e: DownloadEventsException) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = EventFailures.DownloadEventsFailure)
    } catch (e: Exception) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = Failure.UnknownFailure)
    }
}