package com.cornershop.counterstest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cornershop.counterstest.data.counters.dao.CountersDao
import com.cornershop.counterstest.domain.counters.entity.Counter

@Database(entities = [Counter::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun countersDao(): CountersDao
}