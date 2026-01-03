package com.example.data.repository

import androidx.room.Transaction
import com.example.data.database.daos.IncidentDao
import com.example.data.database.daos.MetadataDao
import com.example.data.dtos.toEntity
import com.example.domain.logger.IMeliLogger
import com.example.domain.models.IncidentTracker
import com.example.domain.repositories.IncidentTrackerRepository
import com.example.domain.service.IObservabilityService
import io.ktor.client.statement.HttpResponse

class IncidentTrackerRepositoryImpl(
  private val incidentDao : IncidentDao,
  private val metadataDao : MetadataDao,
  private val logger : IMeliLogger,
  private val api : IObservabilityService,
): IncidentTrackerRepository {
  @Transaction
  override suspend fun insertIncidentTracker(incidentTracker : IncidentTracker) {
    logger.debug("IncidentTrackerRepositoryImpl::insertIncidentTracker")
    val incidentEntity = incidentTracker.toEntity()
    incidentDao.create(incidentEntity)
    incidentTracker.metadata.forEach { metadata ->
      metadataDao.create(metadata.toEntity(incidentEntity.id))
    }
    api.addIncidentTrack<HttpResponse>(incidentTracker).onSuccess {
      incidentDao.upsert(incidentEntity.copy(isSync = true))
      incidentTracker.metadata.forEach { metadata ->
        metadataDao.upsert(metadata.toEntity(incidentEntity.id).copy(isSync = true))
      }
    }.onFailure {
      logger.error("IncidentTrackerRepositoryImpl::insertIncidentTracker", it)
    }
  }
}