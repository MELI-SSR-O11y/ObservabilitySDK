package com.example.data.service

import com.example.data.networking.Failure
import com.example.data.networking.doPost
import com.example.domain.logger.IMeliLogger
import com.example.domain.service.ObservabilityService
import com.example.domain.util.DataError
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse

class ObservabilityService(
  private val logger: IMeliLogger,
  private val httpClient: HttpClient
): ObservabilityService {
  override suspend fun addScreen(name : String): Result<Any> {
    logger.debug("ObservabilityService::addScreen")
    return httpClient.doPost<Result<HttpResponse>>(
      route = "api/observability/addScreen",
      logger = logger,
      body = "Screen(name = name, id = )"
    ).onFailure {
      val failure = it as Failure

      when(failure.error) {
        is DataError.Remote -> {
          logger.critical("RemotePost::addScreen",failure.message ?: "Unknown error", failure)
        }
        is DataError.Connection -> {
          logger.error(failure.message ?: "Unknown error", failure)
        }
        else -> logger.error("RemotePost::addScreen -> " + failure.message)
      }
    }
  }

  override suspend fun addIncidentTrack(body : String) {
    logger.debug("ObservabilityService::addIncidentTrack")
    TODO("Not yet implemented")
  }

  override suspend fun getAllScreens() : List<String> {
    logger.debug("ObservabilityService::getAllScreens")
    TODO("Not yet implemented")
  }

  override suspend fun backupScreens(body : String) {
    logger.debug("ObservabilityService::backupScreens00")
    TODO("Not yet implemented")
  }
}