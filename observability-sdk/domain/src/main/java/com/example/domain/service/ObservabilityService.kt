package com.example.domain.service

interface ObservabilityService {
  suspend fun addScreen(name: String)
  suspend fun addIncidentTrack(body: String)
  suspend fun getAllScreens()
  suspend fun backupScreens(body : String)
}