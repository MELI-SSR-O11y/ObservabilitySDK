package com.example.data.repository

import com.example.data.database.MeliDatabase
import com.example.data.dtos.toEntity
import com.example.domain.logger.IMeliLogger
import com.example.domain.models.IncidentTracker
import com.example.domain.repositories.IncidentTrackerRepository
import com.example.domain.service.IObservabilityService
import io.ktor.client.statement.HttpResponse

class IncidentTrackerRepositoryImpl(
  private val incidentDao : MeliDatabase,
  private val logger : IMeliLogger,
  private val api : IObservabilityService,
): IncidentTrackerRepository {
  override suspend fun insertIncidentTracker(incidentTracker : IncidentTracker) {
    logger.debug("IncidentTrackerRepositoryImpl::insertIncidentTracker")
    val incidentEntity = incidentTracker.toEntity()
    incidentDao.incidentDao().create(incidentEntity)
    api.addIncidentTrack<HttpResponse>(incidentTracker).onSuccess {
      incidentDao.incidentDao().upsert(incidentEntity.copy(isSync = true))
    }.onFailure {
      logger.error("IncidentTrackerRepositoryImpl::insertIncidentTracker", it)
    }
  }
}