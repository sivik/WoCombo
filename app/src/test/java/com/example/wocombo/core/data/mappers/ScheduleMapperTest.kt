package com.example.wocombo.core.data.mappers

import com.example.wocombo.core.data.adapters.remote.rest.response.EventsItemRemoteModel
import com.example.wocombo.core.data.model.EventEntity
import com.example.wocombo.core.data.model.ScheduleEntity
import com.example.wocombo.core.domain.models.Event
import com.example.wocombo.core.domain.models.Schedule
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class ScheduleMapperTest{
    @Test
    fun `EventRemoteModel map to entity`() {
        val model = EventsItemRemoteModel(
            id = "1",
            date = "2021-12-12T10:08:37.118Z",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310798917361_image-header_pDach_1554667420000.jpeg",
            subtitle = "Soccer",
            title = "Liverpool vs AS Monaco",
            videoUrl ="https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
        )

        val expected = EventEntity(
            id = "1",
            date = "2021-12-12T10:08:37.118Z",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310798917361_image-header_pDach_1554667420000.jpeg",
            subtitle = "Soccer",
            title = "Liverpool vs AS Monaco",
            videoUrl ="https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
        )
        val result = model.mapToEntity()
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `ScheduleEntity map to domain`() {
        val model = ScheduleEntity(
            id = "1",
            date = "2020-01-01T01:01:01.001Z",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310798917361_image-header_pDach_1554667420000.jpeg",
            subtitle = "Soccer",
            title = "Liverpool vs AS Monaco"
        )

        val expected = Schedule(
            id = "1",
            date = DateTime(2020, 1, 1, 1, 1, 1, 1),
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310798917361_image-header_pDach_1554667420000.jpeg",
            subtitle = "Soccer",
            title = "Liverpool vs AS Monaco",
        )
        val result = model.mapToDomain()
        Assertions.assertEquals(expected, result)
    }
}