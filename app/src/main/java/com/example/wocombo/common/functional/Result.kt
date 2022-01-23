package com.example.wocombo.common.functional

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null,
    val failure: Failure? = null
) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: String? = null, data: T? = null, failure: Failure) :
        Result<T>(data, message, failure)

    class Loading<T>(data: T? = null) : Result<T>(data)
}