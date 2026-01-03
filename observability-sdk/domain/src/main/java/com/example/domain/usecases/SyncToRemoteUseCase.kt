package com.example.domain.usecases

import com.example.domain.repositories.ScreenRepository

class SyncToRemoteUseCase(private val screenRepository: ScreenRepository) {
  suspend operator fun invoke() {
    return screenRepository.syncToRemote()
  }
}