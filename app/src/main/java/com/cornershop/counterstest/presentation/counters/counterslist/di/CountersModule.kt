package com.cornershop.counterstest.presentation.counters.counterslist.di

import com.cornershop.counterstest.presentation.counters.counterslist.viewmodel.CountersViewModelDelegate
import com.cornershop.counterstest.presentation.counters.counterslist.viewmodel.CountersViewModelDelegateImpl
import com.cornershop.counterstest.presentation.counters.counterslist.viewmodel.UpdateCountersViewModelDelegate
import com.cornershop.counterstest.presentation.counters.counterslist.viewmodel.UpdateCountersViewModelDelegateImpl
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
