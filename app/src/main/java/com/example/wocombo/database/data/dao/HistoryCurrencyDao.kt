package com.example.wocombo.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wocombo.database.data.model.HistoryCurrencyDbModel

@Dao
interface HistoryCurrencyDao : BaseDao<HistoryCurrencyDbModel> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(vararg obj: HistoryCurrencyDbModel): Array<Long>

    @Query("SELECT * FROM history_currencies WHERE currency_id = :currencyId")
    fun getAllHistoryByCurrencyId(currencyId: Int): List<HistoryCurrencyDbModel>

}