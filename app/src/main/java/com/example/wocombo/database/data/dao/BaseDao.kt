package com.example.wocombo.database.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {

    @Insert
    fun insert(vararg obj: T): Array<Long>

    @Update
    fun update(vararg obj: T)

    @Delete
    fun delete(vararg obj: T)
}