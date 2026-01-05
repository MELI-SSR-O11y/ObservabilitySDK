package com.example.domain.usecases

import com.example.domain.models.IncidentFilter
import com.example.domain.models.Screen
import com.example.domain.models.TimeFilter
import com.example.domain.repositories.ScreenRepository
import com.example.domain.util.EIncidentSeverity
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FilterDataUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var screenRepository: ScreenRepository

    private lateinit var filterDataUseCase: FilterDataUseCase

    @Before
    fun setUp() {
        filterDataUseCase = FilterDataUseCase(screenRepository)
    }

    @Test
    fun `invoke should call repository with correct filter and return its flow`() = runTest {
        // Given
        val testFilter = IncidentFilter(screenId = "test-screen-id")
        val expectedScreens = listOf(Screen(id = "test-screen-id", name = "Test Screen", incidentTrackers = emptyList(), isSync = true))
        val expectedFlow = flowOf(expectedScreens)

        every { screenRepository.getScreensByFilter(testFilter) } returns expectedFlow

        // When
        val resultFlow = filterDataUseCase(testFilter)

        // Then
        verify(exactly = 1) { screenRepository.getScreensByFilter(testFilter) }
        val resultData = resultFlow.first()
        assertEquals(expectedScreens, resultData)
    }

    @Test
    fun `invoke should handle TimeFilter correctly`() = runTest {
        // Given
        val timeFilter = TimeFilter.Last5Minutes
        val testFilter = IncidentFilter(timeFilter = timeFilter)
        val expectedScreens = listOf(Screen(id = "screen-1", name = "Screen with recent incidents", incidentTrackers = emptyList(), isSync = false))
        val expectedFlow = flowOf(expectedScreens)

        every { screenRepository.getScreensByFilter(testFilter) } returns expectedFlow

        // When
        val resultFlow = filterDataUseCase(testFilter)

        // Then
        verify(exactly = 1) { screenRepository.getScreensByFilter(testFilter) }
        val resultData = resultFlow.first()
        assertEquals(expectedScreens, resultData)
    }

    @Test
    fun `invoke should handle SeverityFilter correctly`() = runTest {
        // Given
        val severityFilter = EIncidentSeverity.ERROR
        val testFilter = IncidentFilter(severity = severityFilter)
        val expectedScreens = listOf(Screen(id = "screen-2", name = "Screen with error incidents", incidentTrackers = emptyList(), isSync = false))
        val expectedFlow = flowOf(expectedScreens)

        every { screenRepository.getScreensByFilter(testFilter) } returns expectedFlow

        // When
        val resultFlow = filterDataUseCase(testFilter)

        // Then
        verify(exactly = 1) { screenRepository.getScreensByFilter(testFilter) }

        val resultData = resultFlow.first()
        assertEquals(expectedScreens, resultData)
    }
}
