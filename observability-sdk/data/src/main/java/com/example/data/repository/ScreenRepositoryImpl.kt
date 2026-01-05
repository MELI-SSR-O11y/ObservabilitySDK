@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.data.repository

import android.util.Log
import androidx.room.Transaction
import com.example.data.database.daos.IncidentDao
import com.example.data.database.daos.MetadataDao
import com.example.data.database.daos.ScreenDao
import com.example.data.database.entities.ScreenEntity
import com.example.data.dtos.toEntity
import com.example.data.dtos.toIncidentTracker
import com.example.data.dtos.toMetadata
import com.example.data.dtos.toScreen
import com.example.domain.service.IObservabilityService
import com.example.domain.logger.IMeliLogger
import com.example.domain.models.IncidentFilter
import com.example.domain.models.Screen
import com.example.domain.models.TimeFilter
import com.example.domain.repositories.ScreenRepository
import com.example.domain.util.EIncidentSeverity
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ScreenRepositoryImpl(
  private val screenDao : ScreenDao,
  private val incidentDao : IncidentDao,
  private val metadataDao : MetadataDao,
  private val api : IObservabilityService,
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

  override fun getScreensByFilter(filter: IncidentFilter) : Flow<List<Screen>> {
    return screenDao.getScreensWithRelations().flatMapLatest { screensWithIncidents ->
      val filterScreens: List<Screen> = screensWithIncidents.filter { screen ->
        screen.screen.id == filter.screenId || filter.screenId == null
      }.map { screenEntity ->
        screenEntity.screen.toScreen(
          incidentTrackers = screenEntity.incidents.filter { incident ->
            val timeFilter = if(filter.timeFilter is TimeFilter.None) {
              0
            } else {
              System.currentTimeMillis() - filter.timeFilter.durationMillis
            }
            incident.incident.timestamp >= timeFilter
                    && (filter.severity == null
                    || EIncidentSeverity.valueOf(incident.incident.severity) == filter.severity)
           }.map { incident ->
             incident.incident.toIncidentTracker(
               metadata = incident.metadata.map { metadataEntity -> metadataEntity.toMetadata() }
             )
          }
        )
      }
      flow {
        emit(filterScreens)
      }
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

  @Transaction
  override suspend fun syncToRemote() {
    logger.debug("ScreenRepositoryImpl::syncToRemote::Start")
    val metadataNotPushEntity = metadataDao.getAllNotSync()
    val incidentNotPushEntity = incidentDao.getAllNotSync()
    val screensNotPushEntity = screenDao.getAllNotSync()

    val screens = screensNotPushEntity.map { screen ->
      screen.toScreen(incidentNotPushEntity.filter { incident -> incident.pkScreen == screen.id }
        .map { incident ->
          incident.toIncidentTracker(metadataNotPushEntity.filter { metadata ->
            metadata.pkIncident == incident.id
          }.map {
            it.toMetadata()
          })
        })
    }

    api.pushScreens<HttpResponse>(screens).onSuccess {
      metadataNotPushEntity.forEach { metadata ->
        metadataDao.upsert(metadata.copy(isSync = true))
      }
      incidentNotPushEntity.forEach { incident ->
        incidentDao.upsert(incident.copy(isSync = true))
      }
      screensNotPushEntity.forEach { screen ->
        screenDao.upsert(screen.copy(isSync = true))
      }
    }
  }

  override suspend fun rollbackFromRemote() {
    api.rollbackFromRemote<HttpResponse>().onSuccess { response ->
      try {
        Log.d("rollbackFromRemote", response.body<List<Screen>>().toString())
        val screens = response.body<List<Screen>>()
        screens.forEach { screen ->
          screenDao.create(screen.toEntity())
          screen.incidentTrackers.forEach { incident ->
            incidentDao.create(incident.toEntity().copy (isSync = true))
            incident.metadata.forEach { metadata ->
              metadataDao.create(metadata.toEntity(incident.id).copy(isSync = true))
            }
          }
        }
      } catch(e: Exception) {
        Log.d("rollbackFromRemote", e.message.toString())
      }
    }
  }

}