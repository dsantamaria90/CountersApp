package com.cornershop.counterstest.testutil

import io.mockk.MockKAnnotations
import org.junit.Before

abstract class MockKTest {

    @Before
    fun initMocks() {
        MockKAnnotations.init(this)
    }
}