package com.cornershop.counterstest.presentation.util.share

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
abstract class ShareModule {

    @Binds
    abstract fun bindShareDelegate(delegate: ShareDelegateImpl): ShareDelegate
}
