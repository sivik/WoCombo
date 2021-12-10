package com.example.wocombo.core.data.adapters.remote.rest.response

class EventsRemoteModel : ArrayList<EventsItemRemoteModel>()

data class EventsItemRemoteModel(
    val date: String,
    val id: String,
    val imageUrl: String,
    val subtitle: String,
    val title: String,
    val videoUrl: String
)