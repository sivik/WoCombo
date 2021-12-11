package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.common.functional.UseCase

class LoginUseCase(
   //private val repository: AuthorizeRepository
) : UseCase<LoginUseCase.Request, LoginUseCase.Result> {

    data class Request(
        val username: String,
        val password: String
    ) : UseCase.UseCaseRequest

    data class Result(
        val success: Boolean? = false,
        val failure: Failure? = null
    ) : UseCase.UseCaseResponse

    override fun execute(request: Request): Result = try {
        //Any authorization login code
        Result(success = true)
    } catch (e: Exception) {
        logUseCaseError(e, javaClass.simpleName)
        Result(failure = Failure.UnknownFailure)
    }
}