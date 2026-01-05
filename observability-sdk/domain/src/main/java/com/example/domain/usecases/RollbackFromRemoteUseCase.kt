package com.example.domain.usecases

import com.example.domain.repositories.ScreenRepository

class RollbackFromRemoteUseCase(private val screenRepository: ScreenRepository) {
  suspend operator fun invoke() {
    return screenRepository.rollbackFromRemote()
  }
}