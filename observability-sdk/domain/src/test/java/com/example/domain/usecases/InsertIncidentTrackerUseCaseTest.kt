package com.example.domain.usecases

import com.example.domain.models.IncidentTracker
import com.example.domain.repositories.IncidentTrackerRepository
import com.example.domain.util.EIncidentSeverity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFailsWith

class InsertIncidentTrackerUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var incidentTrackerRepository: IncidentTrackerRepository

    private lateinit var insertIncidentTrackerUseCase: InsertIncidentTrackerUseCase

    @Before
    fun setUp() {
        insertIncidentTrackerUseCase = InsertIncidentTrackerUseCase(incidentTrackerRepository)
    }

    @Test
    fun `invoke should call repository to insert incident`() = runTest {
        // Given
        val testIncident = IncidentTracker(
            errorCode = 500,
            message = "Server error",
            severity = EIncidentSeverity.ERROR,
            screenId = "screen-123",
            isSync = true,
            timestamp = System.currentTimeMillis(),
            metadata = emptyList()
        )
        val screenName = "LoginScreen"

        // When
        insertIncidentTrackerUseCase(testIncident, screenName)

        // Then
        coVerify(exactly = 1) { incidentTrackerRepository.insertIncidentTracker(testIncident, screenName) }
    }

    @Test
    fun `invoke should propagate exception when repository fails`() = runTest {
        // Given
        val testIncident = IncidentTracker(
            errorCode = 404,
            message = "Not Found",
            severity = EIncidentSeverity.WARNING,
            screenId = "screen-404",
            isSync = true,
            timestamp = System.currentTimeMillis(),
            metadata = emptyList()
        )
        val screenName = "NotFoundScreen"
        val errorMessage = "Database write failed"
        coEvery { incidentTrackerRepository.insertIncidentTracker(any(), any()) } throws RuntimeException(errorMessage)

        // When & Then
        val exception = assertFailsWith<RuntimeException> {
            insertIncidentTrackerUseCase(testIncident, screenName)
        }
        assertEquals(errorMessage, exception.message)

        coVerify(exactly = 1) { incidentTrackerRepository.insertIncidentTracker(testIncident, screenName) }
    }

    @Test
    fun `invoke should handle edge case with empty message`() = runTest {
        // Given
        val edgeCaseIncident = IncidentTracker(
            errorCode = 0,
            message = "",
            severity = EIncidentSeverity.DEBUG,
            screenId = "screen-abc",
            isSync = true,
            timestamp = System.currentTimeMillis(),
            metadata = emptyList()
        )
        val screenName = "DebugScreen"

        // When
        insertIncidentTrackerUseCase(edgeCaseIncident, screenName)

        // Then
        coVerify(exactly = 1) { incidentTrackerRepository.insertIncidentTracker(edgeCaseIncident, screenName) }
    }
}
