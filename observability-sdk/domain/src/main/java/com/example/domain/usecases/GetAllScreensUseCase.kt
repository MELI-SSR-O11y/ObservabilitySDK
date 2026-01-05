package com.example.domain.usecases

import com.example.domain.models.Screen
import com.example.domain.repositories.ScreenRepository
import kotlinx.coroutines.flow.Flow

class GetAllScreensUseCase(private val screenRepository: ScreenRepository) {

    operator fun invoke(): Flow<List<Screen>> {
        return screenRepository.getAllScreens()
    }
}
