package com.example.wocombo.common.functional

import android.util.Log
import kotlinx.coroutines.flow.Flow
import java.io.ByteArrayOutputStream
import java.io.PrintStream

abstract class FlowUseCase<T, in RQ> where T : FlowUseCase.FlowResponse, RQ : FlowUseCase.FlowRequest {
    abstract operator fun invoke(request: RQ): Flow<Result<T>>
    interface FlowRequest
    interface FlowResponse

    fun logUseCaseError(e: Exception, tag: String) {
        ByteArrayOutputStream().use {
            val stream = PrintStream(it)
            e.printStackTrace(stream)
            Log.e(tag, String(it.toByteArray()))
        }
    }
}