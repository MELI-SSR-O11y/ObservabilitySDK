package com.example.presentation.main

import com.example.domain.models.IncidentTracker

sealed class MainScreenContract {
    data class InsertScreen(val name: String) : MainScreenContract()
    data class InsertIncident(val incident: IncidentTracker) : MainScreenContract()
}
