package com.cornershop.counterstest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cornershop.counterstest.data.dao.CountersDao
import com.cornershop.counterstest.domain.entity.Counter

@Database(entities = [Counter::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countersDao(): CountersDao
}