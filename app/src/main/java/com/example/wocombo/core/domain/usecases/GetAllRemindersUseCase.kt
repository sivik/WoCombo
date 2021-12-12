package com.example.wocombo.core.domain.usecases

import androidx.paging.PagingData
import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.UseCase
import com.example.wocombo.core.domain.models.Reminder
import com.example.wocombo.core.domain.repositories.ReminderRepository
import kotlinx.coroutines.flow.Flow

class GetAllRemindersUseCase (
    private val reminderRepository: ReminderRepository
) : UseCase<GetAllRemindersUseCase.Request, GetAllRemindersUseCase.Result> {

    class Request : UseCase.UseCaseRequest

    data class Result(
        val pagingData: Flow<PagingData<Reminder>>? = null,
        val failure: Failure? = null
    ) : UseCase.UseCaseResponse

    override fun execute(request: Request): Result {
        return try {
            val pagingData = reminderRepository.getRemindersStream()
            Result(pagingData = pagingData)
        } catch (e: Exception) {
            logUseCaseError(e, javaClass.simpleName)
            Result(failure = Failure.UnknownFailure)
        }
    }
}