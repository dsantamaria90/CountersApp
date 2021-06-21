package com.cornershop.counterstest.data.di

import com.cornershop.counterstest.data.service.CountersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.4:3000/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun providesDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun providesCountersService(retrofit: Retrofit): CountersService =
        retrofit.create(CountersService::class.java)
}