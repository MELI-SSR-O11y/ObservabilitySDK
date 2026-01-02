package com.example.data.repository

import com.example.data.database.daos.IncidentDao
import com.example.domain.models.IncidentTracker
import com.example.domain.repositories.IncidentTrackerRepository

class IncidentTrackerRepositoryImpl(private val incidentDao: IncidentDao): IncidentTrackerRepository {
  override suspend fun insertIncidentTracker(incidentTracker : IncidentTracker) {
    TODO("Not yet implemented")
  }
}