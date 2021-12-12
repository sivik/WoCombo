package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.UseCase
import com.example.wocombo.core.domain.repositories.ReminderRepository

class DeleteReminderUseCase(
    private val reminderRepository: ReminderRepository
) : UseCase<DeleteReminderUseCase.Request, DeleteReminderUseCase.Result> {

    data class Request(
        val reminderId: Int
    ): UseCase.UseCaseRequest

    data class Result(
        val reminderId: Int? = null,
        val failure: Failure? = null
    ): UseCase.UseCaseResponse

    override fun execute(request: Request): Result {
        return try {
            reminderRepository.deleteReminder(request.reminderId)
            return Result(reminderId = request.reminderId)
        } catch (e: Exception) {
            logUseCaseError(e, javaClass.simpleName)
            Result(failure = Failure.UnknownFailure)
        }
    }
}