package com.example.wocombo.core.domain.errors

import com.example.wocombo.common.functional.Failure

class ScheduleFailures{
    object InternalServerFailure : Failure.FeatureFailure()
}