package com.cornershop.counterstest.data.di

import android.content.Context
import androidx.room.Room
import com.cornershop.counterstest.data.AppDatabase
import com.cornershop.counterstest.data.counters.dao.CountersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "counters.db"
        ).build()

    @Singleton
    @Provides
    fun providesCountersDao(appDatabase: AppDatabase): CountersDao = appDatabase.countersDao()
}