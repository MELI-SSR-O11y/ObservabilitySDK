package com.example.domain.usecases

import com.example.domain.repositories.ScreenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InsertScreenUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var screenRepository: ScreenRepository

    private lateinit var insertScreenUseCase: InsertScreenUseCase

    @Before
    fun setUp() {
        insertScreenUseCase = InsertScreenUseCase(screenRepository)
    }

    @Test
    fun `invoke should call repository to insert screen`() = runTest {
        // Given
        val screenName = "LoginScreen"

        // When
        insertScreenUseCase(screenName)

        // Then
        coVerify(exactly = 1) { screenRepository.insertScreen(screenName) }
    }

    @Test
    fun `invoke should propagate exception when repository fails`() = runTest {
        // Given
        val errorMessage = "Database connection failed"
        coEvery { screenRepository.insertScreen(any()) } throws RuntimeException(errorMessage)

        // When & Then
        val exception = assertFailsWith<RuntimeException> {
            insertScreenUseCase("ErrorScreen")
        }
        Assert.assertEquals(errorMessage, exception.message)

        coVerify(exactly = 1) { screenRepository.insertScreen("ErrorScreen") }
    }

    @Test
    fun `invoke should call repository even with empty screen name`() = runTest {
        // Given
        val emptyScreenName = ""

        // When
        insertScreenUseCase(emptyScreenName)
        // Then
        coVerify(exactly = 1) { screenRepository.insertScreen(emptyScreenName) }
    }
}
