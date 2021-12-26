package com.example.wocombo.core.data.adapters.remote.rest.api

import com.example.wocombo.common.network.DaznApiConstants
import com.example.wocombo.core.data.adapters.remote.rest.response.EventsRemoteModel
import com.example.wocombo.core.data.adapters.remote.rest.response.SchedulesRemoteModel
import retrofit2.Call
import retrofit2.http.GET


interface EventApi {
    @GET(DaznApiConstants.GET_EVENTS)
    fun downloadEvents(): Call<EventsRemoteModel>
}

interface ScheduleApi {
    @GET(DaznApiConstants.GET_SCHEDULE)
    fun downloadSchedules(): Call<SchedulesRemoteModel>
}