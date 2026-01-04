package com.example.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.IncidentFilter
import com.example.domain.usecases.FilterDataUseCase
import com.example.domain.usecases.GetAllScreensUseCase
import com.example.domain.usecases.InsertIncidentTrackerUseCase
import com.example.domain.usecases.InsertScreenUseCase
import com.example.domain.usecases.SyncToRemoteUseCase
import com.example.domain.util.EIncidentSeverity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class ContractViewModel(
    private val insertIncidentTrackerUseCase: InsertIncidentTrackerUseCase,
    private val insertScreenUseCase: InsertScreenUseCase,
    private val syncToRemoteUseCase: SyncToRemoteUseCase,
    private val getAllScreensUseCase: GetAllScreensUseCase,
    private val filterUseCase: FilterDataUseCase,
) : ViewModel(), ContractObservabilityApi {

    private val _internalState = MutableStateFlow(MainState())
    private val _filter = MutableStateFlow(IncidentFilter())

    override val state: StateFlow<MainState> = _filter
        .flatMapLatest { filter ->
            combine(
                getAllScreensUseCase(),
                filterUseCase(filter),
                _internalState
            ) { allScreens, filteredScreens, internalState ->
                val allIncidents = filteredScreens.flatMap { it.incidentTrackers }
                internalState.copy(
                    screens = allScreens,
                    isLoading = internalState.isLoading,
                    incidentsQuantity = allIncidents.size,
                    screensQuantity = allScreens.size,
                    debugSeverityQuantity = allIncidents.count { it.severity == EIncidentSeverity.DEBUG },
                    infoSeverityQuantity = allIncidents.count { it.severity == EIncidentSeverity.INFO },
                    warningSeverityQuantity = allIncidents.count { it.severity == EIncidentSeverity.WARNING },
                    errorSeverityQuantity = allIncidents.count { it.severity == EIncidentSeverity.ERROR },
                    criticalSeverityQuantity = allIncidents.count { it.severity == EIncidentSeverity.CRITICAL },
                    isSync = !(allScreens.any { !it.isSync } || allIncidents.any { !it.isSync } || allIncidents.any { it.metadata.any { meta -> !meta.isSync } }),
                    activeFilter = filter
                )
            }
        }
        .catch { throwable ->
            emit(_internalState.value.copy(error = throwable.message, isLoading = false))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainState()
        )

    override fun onEvent(event: MainActions) {
        _internalState.update { it.copy(isLoading = true) }
        when (event) {
            is MainActions.InsertScreen -> insertScreen(event.name)
            is MainActions.InsertIncident -> insertIncident(event.incident, event.screenName)
            is MainActions.SyncToRemote -> viewModelScope.launch { syncToRemoteUseCase() }
            is MainActions.FilterByScreen -> _filter.update { it.copy(screenId = event.screenId) }
            is MainActions.FilterBySeverity -> _filter.update { it.copy(severity = event.severity) }
            is MainActions.FilterByTime -> _filter.update { it.copy(timeFilter = event.timeFilter) }
        }
        viewModelScope.launch {
            delay(1000)
            _internalState.update { it.copy(isLoading = false) }
        }
    }

    private fun insertScreen(name: String) {
        viewModelScope.launch {
            insertScreenUseCase(name)
        }
    }

    private fun insertIncident(incident: com.example.domain.models.IncidentTracker, screenName: String) {
        viewModelScope.launch {
            insertIncidentTrackerUseCase(incident, screenName)
        }
    }
}