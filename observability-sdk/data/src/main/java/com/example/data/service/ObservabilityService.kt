package com.example.data.service

import com.example.data.networking.doGet
import com.example.data.networking.doPost
import com.example.domain.logger.IMeliLogger
import com.example.domain.models.IncidentTracker
import com.example.domain.models.Screen
import com.example.domain.service.IObservabilityService
import io.ktor.client.HttpClient
import kotlin.Result

class ObservabilityService(
  private val logger: IMeliLogger,
  private val httpClient: HttpClient
): IObservabilityService {
  override suspend fun <HttpResponse> addScreen(screen : Screen) : Result<HttpResponse> {
    logger.debug("ObservabilityService::addScreen")
    return httpClient.doPost(
      route = "api/observability/addScreen",
      logger = logger,
      body = screen
    )
  }

  override suspend fun <HttpResponse> addIncidentTrack(incidentTrack: IncidentTracker) : Result<HttpResponse> {
    logger.debug("ObservabilityService::addIncidentTrack")
    return httpClient.doPost(
      route = "api/observability/addIncidentTrack",
      logger = logger,
      body = incidentTrack
    )
  }

  override suspend fun <HttpResponse> getAllScreens() : Result<HttpResponse> {
    logger.debug("ObservabilityService::getAllScreens")
    return httpClient.doGet(
      route = "api/observability/getAllScreens",
      logger = logger
    )
  }

  override suspend fun <HttpResponse> pushScreens(screen : List<Screen>) : Result<HttpResponse> {
    logger.debug("ObservabilityService::backupScreens00")
    return httpClient.doPost(
      route = "api/observability/backupScreens",
      logger = logger,
      body = screen
    )
  }
}