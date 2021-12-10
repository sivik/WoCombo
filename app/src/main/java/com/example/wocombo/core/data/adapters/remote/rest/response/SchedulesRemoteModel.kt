package com.example.wocombo.core.data.adapters.remote.rest.response

class SchedulesRemoteModel : ArrayList<ScheduleItemRemoteModel>()

data class ScheduleItemRemoteModel(
    val date: String,
    val id: String,
    val imageUrl: String,
    val subtitle: String,
    val title: String
)