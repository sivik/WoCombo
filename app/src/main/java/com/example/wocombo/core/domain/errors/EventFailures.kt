package com.example.wocombo.core.domain.errors

import com.example.wocombo.common.functional.Failure

sealed class EventFailures {
    object InternalServerFailure : Failure.FeatureFailure()
}