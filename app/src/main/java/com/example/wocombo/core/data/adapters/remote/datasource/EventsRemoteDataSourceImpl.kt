package com.example.wocombo.core.data.adapters.remote.datasource

import android.util.Log
import com.example.wocombo.common.logs.LoggerHelper
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.core.data.adapters.remote.rest.api.EventApi
import com.example.wocombo.core.data.datasources.remote.EventsRemoteDataSource
import com.example.wocombo.core.data.exceptions.DownloadEventsException
import com.example.wocombo.core.data.mappers.mapToEntity
import com.example.wocombo.core.data.model.EventEntity

class EventsRemoteDataSourceImpl(
    private val api: EventApi
) : EventsRemoteDataSource {

    private val tag = LoggerTags.EVENT

    override fun downloadEvents(): List<EventEntity> {
        Log.d(tag, "Start download event list")
        val response = api.downloadEvents().execute()
        if (response.isSuccessful) {
            Log.d(tag, "Event list download successfully")
            return response.body()?.map { it.mapToEntity() }
                ?: throw DownloadEventsException("No body in response from server")
        } else {
            LoggerHelper.logRemoteError(
                tag,
                response.code(),
                response.message(),
                response.errorBody()?.string() ?: ""
            )
            throw DownloadEventsException("Cannot download schedule list from server")
        }
    }
}