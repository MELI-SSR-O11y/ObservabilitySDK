package com.example.data.repository

import com.example.data.database.daos.ScreenDao
import com.example.data.database.entities.ScreenEntity
import com.example.data.dtos.toIncidentTracker
import com.example.data.dtos.toMetadata
import com.example.data.dtos.toScreen
import com.example.domain.service.IObservabilityService
import com.example.domain.logger.IMeliLogger
import com.example.domain.models.Screen
import com.example.domain.repositories.ScreenRepository
import io.ktor.client.statement.HttpResponse
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScreenRepositoryImpl(
  private val screenDao : ScreenDao,
  private val service : IObservabilityService,
  private val logger : IMeliLogger
): ScreenRepository {

  @ExperimentalUuidApi
  override suspend fun insertScreen(name : String) {
    logger.debug("ScreenRepositoryImpl::insertScreen")
    if(screenDao.existByName(screen = name)) return
    val newScreen = ScreenEntity(id = Uuid.random().toString(), name = name)
    screenDao.create(newScreen)
    service.addScreen<HttpResponse>(screen = newScreen.toScreen(listOf())).onSuccess {
      screenDao.upsert(newScreen.copy(isSync = true))
      logger.info("ScreenRepositoryImpl::insertScreen -> Se envio la pantalla al servidor")
    }.onFailure {
      logger.warn("ScreenRepositoryImpl::insertScreen -> No se envio la pantalla al servidor")
    }
  }

  override fun getAllScreens() : Flow<List<Screen>> {
    return screenDao.getScreensWithRelations().map { screensWithIncidents ->
      screensWithIncidents.map { screenWithIncidents ->
        val incidents = screenWithIncidents.incidents.map { incidentWithMetadata ->
          val metadata = incidentWithMetadata.metadata.map { it.toMetadata() }
          incidentWithMetadata.incident.toIncidentTracker(metadata)
        }
        screenWithIncidents.screen.toScreen(incidents)
      }
    }
  }

}