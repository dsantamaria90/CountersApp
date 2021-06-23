package com.cornershop.counterstest.presentation.counters.counterslist.di

import com.cornershop.counterstest.presentation.counters.counterslist.delegate.CountersViewModelDelegate
import com.cornershop.counterstest.presentation.counters.counterslist.delegate.CountersViewModelDelegateImpl
import com.cornershop.counterstest.presentation.counters.counterslist.delegate.UpdateCountersViewModelDelegate
import com.cornershop.counterstest.presentation.counters.counterslist.delegate.UpdateCountersViewModelDelegateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CountersModule {

    @Binds
    abstract fun bindCountersViewModelDelegate(
        delegate: CountersViewModelDelegateImpl
    ): CountersViewModelDelegate

    @Binds
    abstract fun bindUpdateCountersViewModelDelegate(
        delegate: UpdateCountersViewModelDelegateImpl
    ): UpdateCountersViewModelDelegate
}
