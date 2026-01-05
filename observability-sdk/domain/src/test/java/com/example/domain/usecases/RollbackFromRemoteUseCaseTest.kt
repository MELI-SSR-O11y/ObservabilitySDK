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

class RollbackFromRemoteUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var screenRepository: ScreenRepository

    private lateinit var rollbackFromRemoteUseCase: RollbackFromRemoteUseCase

    @Before
    fun setUp() {
        rollbackFromRemoteUseCase = RollbackFromRemoteUseCase(screenRepository)
    }

    @Test
    fun `invoke should call rollbackFromRemote on repository`() = runTest {
        // When
        rollbackFromRemoteUseCase()

        // Then
        coVerify(exactly = 1) { screenRepository.rollbackFromRemote() }
    }

    @Test
    fun `invoke should propagate exception when repository fails`() = runTest {
        // Given
        val errorMessage = "Database rollback failed"
        coEvery { screenRepository.rollbackFromRemote() } throws RuntimeException(errorMessage)

        // When & Then
        val exception = assertFailsWith<RuntimeException> {
            rollbackFromRemoteUseCase()
        }
        assertEquals(errorMessage, exception.message)

        // Also Then
        coVerify(exactly = 1) { screenRepository.rollbackFromRemote() }
    }

    @Test
    fun `invoke multiple times should call repository multiple times`() = runTest {
        // Given
        val invocationCount = 5

        // When
        repeat(invocationCount) {
            rollbackFromRemoteUseCase()
        }

        // Then
        coVerify(exactly = invocationCount) { screenRepository.rollbackFromRemote() }
    }
}
