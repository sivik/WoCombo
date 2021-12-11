package com.example.wocombo.core.domain.errors

import com.example.wocombo.common.functional.Failure

sealed class EventFailures {
    object DownloadEventsFailure : Failure.FeatureFailure()
}