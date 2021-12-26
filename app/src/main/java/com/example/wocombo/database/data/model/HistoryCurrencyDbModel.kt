package com.example.wocombo.database.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "history_currencies")
data class HistoryCurrencyDbModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "currency_id")
    val currencyId: Int,
    @ColumnInfo(name = "name_id")
    val nameId: String,
    @ColumnInfo(name = "usd_price")
    val priceUsd: Double,
    @ColumnInfo(name = "date")
    val date: DateTime
)