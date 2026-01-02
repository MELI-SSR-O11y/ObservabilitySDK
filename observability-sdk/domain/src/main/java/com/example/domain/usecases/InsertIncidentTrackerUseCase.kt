package com.example.domain.usecases

import com.example.domain.models.IncidentTracker
import com.example.domain.repositories.IncidentTrackerRepository

class InsertIncidentTrackerUseCase(private val incidentTrackerRepository: IncidentTrackerRepository) {

    suspend operator fun invoke(incidentTracker: IncidentTracker) {
        incidentTrackerRepository.insertIncidentTracker(incidentTracker)
    }
}