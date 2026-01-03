package com.example.domain.repositories

import com.example.domain.models.Screen
import kotlinx.coroutines.flow.Flow

interface ScreenRepository {
  suspend fun insertScreen(name: String)
  fun getAllScreens(): Flow<List<Screen>>
  suspend fun syncToRemote()
}