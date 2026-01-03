package com.example.presentation.main

import com.example.domain.models.IncidentTracker
import com.example.domain.models.Screen

/**
 * Represents the state of the UI. It's a single source of truth for the view.
 */
data class MainState(
    val screens: List<Screen> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSync: Boolean = false,
    val screensQuantity: Int = 0,
    val incidentsQuantity: Int = 0,
    val debugSeverityQuantity: Int = 0,
    val infoSeverityQuantity: Int = 0,
    val warningSeverityQuantity: Int = 0,
    val errorSeverityQuantity: Int = 0,
    val criticalSeverityQuantity: Int = 0
)

/**
 * Represents the events that the view can send to the ViewModel.
 */
sealed class MainActions {
    data class InsertScreen(val name: String) : MainActions()
    data class InsertIncident(val incident: IncidentTracker) : MainActions()
    object SyncToRemote : MainActions()
}
