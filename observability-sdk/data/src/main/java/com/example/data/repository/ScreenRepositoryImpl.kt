package com.example.data.repository

import com.example.domain.models.Screen
import com.example.domain.repositories.ScreenRepository
import kotlinx.coroutines.flow.Flow

class ScreenRepositoryImpl: ScreenRepository {
  override suspend fun insertScreen(name : String) {
    TODO("Not yet implemented")
  }

  override suspend fun getAllScreens() : Flow<List<Screen>> {
    TODO("Not yet implemented")
  }

}