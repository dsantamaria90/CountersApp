package com.cornershop.counterstest.presentation.welcome

import com.cornershop.counterstest.testutil.ViewModelTest
import org.junit.Assert.assertEquals
import org.junit.Test

class WelcomeViewModelTest : ViewModelTest() {

    private val viewModel = WelcomeViewModel()

    @Test
    fun onGetStartedClicked_setGoToCountersList() {
        // Act
        viewModel.onGetStartedClicked().onClicked()

        // Assert
        assertEquals(Unit, viewModel.goToCountersList.value?.peekContent())
    }
}
