package com.example.domain.usecases

import com.example.domain.repositories.ScreenRepository

class InsertScreenUseCase(private val screenRepository: ScreenRepository) {

    suspend operator fun invoke(name: String) {
        screenRepository.insertScreen(name)
    }
}
