package com.example.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.GetAllScreensUseCase
import com.example.domain.usecases.InsertIncidentTrackerUseCase
import com.example.domain.usecases.InsertScreenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val insertIncidentTrackerUseCase: InsertIncidentTrackerUseCase,
    private val insertScreenUseCase: InsertScreenUseCase,
    getAllScreensUseCase: GetAllScreensUseCase
) : ViewModel() {

    private val _internalState = MutableStateFlow(MainScreenAction())

    val state: StateFlow<MainScreenAction> = combine(
        getAllScreensUseCase(),
        _internalState
    ) { screens, internalState ->
      internalState.copy(
        screens = screens,
        isLoading = false,
        incidentsQuantity = screens.sumOf { it.incidentTrackers.size },
        screensQuantity = screens.size,
        debugSeverityQuantity = screens.sumOf { screen -> screen.incidentTrackers.count { it.severity == "DEBUG" } },
        infoSeverityQuantity = screens.sumOf { screen -> screen.incidentTrackers.count { it.severity == "INFO" } },
        warningSeverityQuantity = screens.sumOf { screen -> screen.incidentTrackers.count { it.severity == "WARNING" } },
        errorSeverityQuantity = screens.sumOf { screen -> screen.incidentTrackers.count { it.severity == "ERROR" } },
        criticalSeverityQuantity = screens.sumOf { screen -> screen.incidentTrackers.count { it.severity == "CRITICAL" } },
      )
    }.catch { throwable ->
      emit(_internalState.value.copy(error = throwable.message, isLoading = false))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainScreenAction()
    )

    fun onEvent(action: MainScreenContract) {
        when (action) {
            is MainScreenContract.InsertScreen -> insertScreen(action.name)
            is MainScreenContract.InsertIncident -> insertIncident(action.incident)
        }
    }

    private fun insertScreen(name: String) {
      viewModelScope.launch {
        _internalState.update { it.copy(isLoading = true) }
        insertScreenUseCase(name)
        _internalState.update { it.copy(isLoading = false) }
      }
    }

    private fun insertIncident(incident: com.example.domain.models.IncidentTracker) {
      viewModelScope.launch {
        _internalState.update { it.copy(isLoading = true) }
        insertIncidentTrackerUseCase(incident)
        _internalState.update { it.copy(isLoading = false) }
      }
    }
}