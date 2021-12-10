package com.example.wocombo.common.logs

import android.util.Log

object LoggerHelper {
    fun logRemoteError(tag: String, code: Int, message: String, responseBody: String) {
        Log.e(
            tag,
            "Response : ${code}/${message}.$responseBody\n"
        )
    }
}