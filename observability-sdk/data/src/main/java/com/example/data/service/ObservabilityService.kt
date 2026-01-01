package com.example.data.service

import com.example.data.networking.Screen
import com.example.data.networking.doGet
import com.example.data.networking.doPost
import com.example.domain.logger.IMeliLogger
import com.example.domain.service.ObservabilityService
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse

class ObservabilityService(
  private val logger: IMeliLogger,
  private val httpClient: HttpClient
): ObservabilityService {
  override suspend fun addScreen(name : String) {
    logger.debug("ObservabilityService::addScreen")
    httpClient.doPost<Screen, Result<HttpResponse>>(
      route = "api/observability/addScreen",
      logger = logger,
      body = Screen(id = "vdsgdbfg", name = "vff")
    )
  }

  override suspend fun addIncidentTrack(body : String) {
    logger.debug("ObservabilityService::addIncidentTrack")
    httpClient.doPost<Any, Result<HttpResponse>>(
      route = "api/observability/addIncidentTrack",
      logger = logger,
      body = Any()
    )
  }

  override suspend fun getAllScreens() {
    logger.debug("ObservabilityService::getAllScreens")
    httpClient.doGet<Result<HttpResponse>>(
      route = "api/observability/getAllScreens",
      logger = logger
    )
  }

  override suspend fun backupScreens(body : String) {
    logger.debug("ObservabilityService::backupScreens00")
    httpClient.doPost<Any, Result<HttpResponse>>(
      route = "api/observability/backupScreens",
      logger = logger,
      body = Any()
    )
  }
}