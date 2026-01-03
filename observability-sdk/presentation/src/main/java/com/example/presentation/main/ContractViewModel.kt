package com.example.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.GetAllScreensUseCase
import com.example.domain.usecases.InsertIncidentTrackerUseCase
import com.example.domain.usecases.InsertScreenUseCase
import com.example.domain.util.EIncidentSeverity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContractViewModel(
    private val insertIncidentTrackerUseCase: InsertIncidentTrackerUseCase,
    private val insertScreenUseCase: InsertScreenUseCase,
    getAllScreensUseCase: GetAllScreensUseCase
) : ViewModel(), ContractObservabilityApi {

    private val _internalState = MutableStateFlow(MainState())

    override val state: StateFlow<MainState> = combine(
        getAllScreensUseCase(),
        _internalState
    ) { screens, internalState ->
        val allIncidents = screens.flatMap { it.incidentTrackers }
        internalState.copy(
            screens = screens,
            isLoading = false,
            incidentsQuantity = allIncidents.size,
            screensQuantity = screens.size,
            debugSeverityQuantity = allIncidents.count { it.severity == EIncidentSeverity.DEBUG },
            infoSeverityQuantity = allIncidents.count { it.severity == EIncidentSeverity.INFO },
            warningSeverityQuantity = allIncidents.count { it.severity == EIncidentSeverity.WARNING },
            errorSeverityQuantity = allIncidents.count { it.severity == EIncidentSeverity.ERROR },
            criticalSeverityQuantity = allIncidents.count { it.severity == EIncidentSeverity.CRITICAL },
        )
    }.catch { throwable ->
        emit(_internalState.value.copy(error = throwable.message, isLoading = false))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainState()
    )

    override fun onEvent(action: MainActions) {
        when (action) {
            is MainActions.InsertScreen -> insertScreen(action.name)
            is MainActions.InsertIncident -> insertIncident(action.incident)
        }
    }

    private fun insertScreen(name: String) {
        viewModelScope.launch {
            _internalState.update { it.copy(isLoading = true) }
            insertScreenUseCase(name)
        }
    }

    private fun insertIncident(incident: com.example.domain.models.IncidentTracker) {
        viewModelScope.launch {
            _internalState.update { it.copy(isLoading = true) }
            insertIncidentTrackerUseCase(incident)
        }
    }
}