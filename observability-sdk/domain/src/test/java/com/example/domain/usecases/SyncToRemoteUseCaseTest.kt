package com.example.domain.usecases

import com.example.domain.repositories.ScreenRepository
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

class SyncToRemoteUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var screenRepository: ScreenRepository

    private lateinit var syncToRemoteUseCase: SyncToRemoteUseCase

    @Before
    fun setUp() {
        syncToRemoteUseCase = SyncToRemoteUseCase(screenRepository)
    }

    @Test
    fun `invoke should call syncToRemote on repository`() = runTest {
        // When
        syncToRemoteUseCase()

        // Then
        coVerify(exactly = 1) { screenRepository.syncToRemote() }
    }

    @Test
    fun `invoke should propagate exception when repository fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { screenRepository.syncToRemote() } throws RuntimeException(errorMessage)

        // When & Then
        val exception = assertFailsWith<RuntimeException> {
            syncToRemoteUseCase()
        }
        assertEquals(errorMessage, exception.message)

        // Also Then
        coVerify(exactly = 1) { screenRepository.syncToRemote() }
    }

    @Test
    fun `invoke multiple times should call repository multiple times`() = runTest {
        // Given
        val invocationCount = 3

        // When
        repeat(invocationCount) {
            syncToRemoteUseCase()
        }

        // Then
        coVerify(exactly = invocationCount) { screenRepository.syncToRemote() }
    }
}
