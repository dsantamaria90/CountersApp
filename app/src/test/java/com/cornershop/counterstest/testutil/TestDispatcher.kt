package com.cornershop.counterstest.testutil

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

object TestDispatcher {

    @ExperimentalCoroutinesApi
    fun newTestDispatcher(): CoroutineDispatcher = TestCoroutineDispatcher()
}