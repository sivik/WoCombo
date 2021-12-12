package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.UseCase
import com.example.wocombo.core.data.exceptions.DownloadSchedulesException
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.ScheduleFailures
import com.example.wocombo.core.domain.models.Schedule
import com.example.wocombo.core.domain.repositories.ScheduleRepository
import com.example.wocombo.core.presentation.enums.SortType
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

class DownloadSchedulesUseCase(
    private val repository: ScheduleRepository
) : UseCase<DownloadSchedulesUseCase.Request, DownloadSchedulesUseCase.Result> {

    data class Request(
        val sortType: SortType
    ) : UseCase.UseCaseRequest

    data class Result(
        val scheduleList: List<Schedule>? = null,
        val failure: Failure? = null
    ) : UseCase.UseCaseResponse

    override fun execute(request: Request): Result = try {
        val schedules = repository.downloadSchedules()
        val sortedList = when (request.sortType) {
            SortType.ASCENDING -> schedules.sortedBy { it.date.millis }
            SortType.DESCENDING -> schedules.sortedByDescending { it.date.millis }
        }
        Result(scheduleList = sortedList)
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
    } catch (e: DownloadSchedulesException) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = ScheduleFailures.DownloadSchedulesFailure)
    } catch (e: Exception) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = Failure.UnknownFailure)
    }
}