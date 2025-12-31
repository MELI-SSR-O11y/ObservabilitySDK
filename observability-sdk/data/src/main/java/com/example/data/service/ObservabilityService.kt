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
    return httpClient.doPost<Result<HttpResponse>>(
      route = "api/observability/addScreen",
      body = """{"id: asdasdsa, name": "$name"}"""
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
    TODO("Not yet implemented")
  }

  override suspend fun getAllScreens() : List<String> {
    TODO("Not yet implemented")
  }

  override suspend fun backupScreens(body : String) {
    TODO("Not yet implemented")
  }
}