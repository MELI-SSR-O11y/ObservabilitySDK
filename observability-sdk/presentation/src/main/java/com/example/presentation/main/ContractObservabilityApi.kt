package com.example.presentation.main

import kotlinx.coroutines.flow.StateFlow

/**
 * Defines the contract for the main screen's API. This is the public interface
 * that the consuming application will interact with.
 *
 * It follows the Unidirectional Data Flow (UDF) pattern, exposing a single source
 * of truth for the state and a single entry point for events.
 */
interface ContractObservabilityApi {
    /**
     * The state of the screen, exposed as a StateFlow for reactive consumption.
     */
    val state: StateFlow<MainState>

    /**
     * The single entry point for the view to send events to the ViewModel.
     */
    fun onEvent(event: MainActions)
}
