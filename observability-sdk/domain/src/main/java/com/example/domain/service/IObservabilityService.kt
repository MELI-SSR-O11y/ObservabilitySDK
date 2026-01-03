package com.example.domain.service

import com.example.domain.models.IncidentTracker
import com.example.domain.models.Screen

interface IObservabilityService {
  suspend fun <HttpResponse> addScreen(screen: Screen): Result<HttpResponse>
  suspend fun <HttpResponse> addIncidentTrack(incidentTrack: IncidentTracker): Result<HttpResponse>
  suspend fun <HttpResponse> getAllScreens(): Result<HttpResponse>
  suspend fun <HttpResponse> pushScreens(screen : List<Screen>): Result<HttpResponse>
}