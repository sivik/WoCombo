package com.example.wocombo.core.data.adapters.remote.datasource

import android.util.Log
import com.example.wocombo.common.logs.LoggerHelper
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.core.data.adapters.remote.rest.api.ScheduleApi
import com.example.wocombo.core.data.datasources.remote.ScheduleRemoteDataSource
import com.example.wocombo.core.data.exceptions.DownloadSchedulesException
import com.example.wocombo.core.data.mappers.mapToEntity
import com.example.wocombo.core.data.model.ScheduleEntity

class ScheduleRemoteDataSourceImpl(
    private val api: ScheduleApi
) : ScheduleRemoteDataSource{

    private val tag = LoggerTags.SCHEDULE

    override fun downloadSchedules(): List<ScheduleEntity> {
        Log.d(tag, "Start download schedule list")
        val response = api.downloadSchedules().execute()
        if (response.isSuccessful) {
            Log.d(tag, "Schedule list download successfully")
            return response.body()?.map { it.mapToEntity() }
                ?: throw DownloadSchedulesException("No body in response from server")
        } else {
            LoggerHelper.logRemoteError(
                tag,
                response.code(),
                response.message(),
                response.errorBody()?.string() ?: ""
            )
            throw DownloadSchedulesException("Cannot download schedule list from server")
        }
    }
}