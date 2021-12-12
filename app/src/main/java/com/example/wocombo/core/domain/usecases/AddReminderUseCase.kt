package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.UseCase
import com.example.wocombo.core.domain.models.Reminder
import com.example.wocombo.core.domain.repositories.ReminderRepository

class AddReminderUseCase (
    private val reminderRepository: ReminderRepository
) : UseCase<AddReminderUseCase.Request, AddReminderUseCase.Result> {

    data class Request(
        val reminder: Reminder
    ): UseCase.UseCaseRequest

    data class Result(
        val reminderId: Int? = null,
        val failure: Failure? = null
    ): UseCase.UseCaseResponse

    override fun execute(request: Request): Result {
        return try {
            reminderRepository.addReminder(request.reminder)
            return Result(reminderId = request.reminder.id)
        } catch (e: Exception) {
            logUseCaseError(e, javaClass.simpleName)
            Result(failure = Failure.UnknownFailure)
        }
    }
}