package com.cornershop.counterstest.presentation.util.actionmode

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
abstract class ActionModeModule {

    @Binds
    abstract fun bindActionModeDelegate(delegate: ActionModeDelegateImpl): ActionModeDelegate
}
