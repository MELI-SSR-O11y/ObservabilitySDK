package com.example.domain.repositories

import com.example.domain.models.IncidentTracker

interface IncidentTrackerRepository {
  suspend fun insertIncidentTracker(incidentTracker: IncidentTracker)
}