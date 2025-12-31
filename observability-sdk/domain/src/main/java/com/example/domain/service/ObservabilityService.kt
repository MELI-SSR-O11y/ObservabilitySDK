package com.example.domain.service

interface ObservabilityService {
  suspend fun addScreen(name: String): Result<Any>
  suspend fun addIncidentTrack(body: String)
  suspend fun getAllScreens(): List<String>
  suspend fun backupScreens(body : String)
}