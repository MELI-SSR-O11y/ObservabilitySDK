package com.example.domain.usecases

import com.example.domain.models.IncidentFilter
import com.example.domain.models.Screen
import com.example.domain.repositories.ScreenRepository
import kotlinx.coroutines.flow.Flow

class FilterDataUseCase(private val screenRepository: ScreenRepository) {
  operator fun invoke(filter: IncidentFilter): Flow<List<Screen>> {
    return screenRepository.getScreensByFilter(filter)
  }
}