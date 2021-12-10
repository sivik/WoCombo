package com.example.wocombo.common.functional

import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.PrintStream

interface UseCase<in RQ, out T> where T : Any, RQ : UseCase.UseCaseRequest {

    interface UseCaseRequest
    interface UseCaseResponse

    fun execute(request: RQ): T

    class None {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    fun logUseCaseError(e: Exception, tag: String) {
        ByteArrayOutputStream().use {
            val stream = PrintStream(it)
            e.printStackTrace(stream)
            Log.e(tag, String(it.toByteArray()))
        }
    }
}