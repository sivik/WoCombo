package com.example.wocombo.database.data.converters

import androidx.room.TypeConverter
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class DateConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String?): DateTime? = when (!value.isNullOrEmpty()) {
            true -> DateTime(value, DateTimeZone.getDefault())
            else -> null
        }

        @TypeConverter
        @JvmStatic
        fun toDateString(date: DateTime?): String? = date?.toDateTime(DateTimeZone.UTC)?.toString()
    }
}