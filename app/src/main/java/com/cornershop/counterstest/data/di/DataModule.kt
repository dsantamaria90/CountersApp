package com.cornershop.counterstest.data.di

import com.cornershop.counterstest.data.repository.CountersRepositoryImpl
import com.cornershop.counterstest.domain.repository.CountersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindCountersRepository(repository: CountersRepositoryImpl): CountersRepository
}