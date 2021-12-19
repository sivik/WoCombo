package com.example.wocombo.core.data.mappers

import com.example.wocombo.core.data.model.ReminderEntity
import com.example.wocombo.core.domain.models.Reminder
import com.example.wocombo.database.data.model.ReminderDbModel
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ReminderMapperTest{
    @Test
    fun `ReminderDbModel map to entity`() {
        val model = ReminderDbModel(
            id = 1,
            date = DateTime(2020,2,2,3,4,5 ),
            title = "Liverpool vs AS Monaco",
        )

        val expected = ReminderEntity(
            id = 1,
            date = DateTime(2020,2,2,3,4,5 ),
            title = "Liverpool vs AS Monaco",
        )
        val result = model.mapToEntity()
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `ReminderEntity map to domain`() {
        val model = ReminderEntity(
            id = 1,
            date = DateTime(2020,2,2,3,4,5 ),
            title = "Liverpool vs AS Monaco",
        )

        val expected = Reminder(
            id = 1,
            date = DateTime(2020,2,2,3,4,5 ),
            title = "Liverpool vs AS Monaco",
        )
        val result = model.mapToDomain()
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `ReminderEntity map to dbmodel`() {
        val model = ReminderEntity(
            id = 1,
            date = DateTime(2020,2,2,3,4,5 ),
            title = "Liverpool vs AS Monaco",
        )

        val expected = ReminderDbModel(
            id = 1,
            date = DateTime(2020,2,2,3,4,5 ),
            title = "Liverpool vs AS Monaco",
        )
        val result = model.mapToDbModel()
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `Reminder map to entity`() {
        val model = Reminder(
            id = 1,
            date = DateTime(2020,2,2,3,4,5 ),
            title = "Liverpool vs AS Monaco",
        )

        val expected = ReminderEntity(
            id = 1,
            date = DateTime(2020,2,2,3,4,5 ),
            title = "Liverpool vs AS Monaco",
        )
        val result = model.mapToEntity()
        Assertions.assertEquals(expected, result)
    }
}