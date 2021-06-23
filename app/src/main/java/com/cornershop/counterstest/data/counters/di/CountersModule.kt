package com.cornershop.counterstest.data.counters.di

import com.cornershop.counterstest.data.counters.repository.CountersRepositoryImpl
import com.cornershop.counterstest.domain.counters.repository.CountersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CountersModule {

    @Binds
    abstract fun bindCountersRepository(repository: CountersRepositoryImpl): CountersRepository
}
