package com.example.wocombo.core.domain.errors

import com.example.wocombo.common.functional.Failure


sealed class CommunicationsFailures {
    object NoInternetFailure : Failure.FeatureFailure()
    object ConnectionFailure : Failure.FeatureFailure()
    object InternalServerFailure : Failure.FeatureFailure()
}