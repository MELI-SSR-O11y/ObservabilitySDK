package com.example.domain.usecases

import com.example.domain.models.Screen
import com.example.domain.repositories.ScreenRepository
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFailsWith

class GetAllScreensUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var screenRepository: ScreenRepository

    private lateinit var getAllScreensUseCase: GetAllScreensUseCase

    @Before
    fun setUp() {
        getAllScreensUseCase = GetAllScreensUseCase(screenRepository)
    }

    @Test
    fun `invoke should return flow of screens from repository`() = runTest {
        // Given
        val expectedScreens = listOf(Screen(id = "screen-1", name = "Home", incidentTrackers = emptyList(), isSync = true))
        val expectedFlow = flowOf(expectedScreens)
        every { screenRepository.getAllScreens() } returns expectedFlow

        // When
        val resultFlow = getAllScreensUseCase()

        // Then
        val resultData = resultFlow.first()
        assertEquals(expectedScreens, resultData)
        verify(exactly = 1) { screenRepository.getAllScreens() }
    }

    @Test
    fun `invoke should return empty flow when repository has no screens`() = runTest {
        // Given
        val expectedFlow = flowOf(emptyList<Screen>())
        every { screenRepository.getAllScreens() } returns expectedFlow

        // When
        val resultFlow = getAllScreensUseCase()

        // Then
        val resultData = resultFlow.first()
        assertTrue(resultData.isEmpty())
        verify(exactly = 1) { screenRepository.getAllScreens() }
    }

    @Test
    fun `invoke should propagate exception when repository flow fails`() = runTest {
        // Given
        val errorMessage = "Database error"
        val errorFlow = flow<List<Screen>> { throw RuntimeException(errorMessage) }
        every { screenRepository.getAllScreens() } returns errorFlow

        // When
        val resultFlow = getAllScreensUseCase()

        // Then
        val exception = assertFailsWith<RuntimeException> {
            resultFlow.first()
        }
        assertEquals(errorMessage, exception.message)
        verify(exactly = 1) { screenRepository.getAllScreens() }
    }
}
