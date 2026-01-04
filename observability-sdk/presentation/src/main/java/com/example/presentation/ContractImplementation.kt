package com.example.presentation

import com.example.presentation.main.ContractObservabilityApi
import com.example.presentation.main.ContractViewModel
import com.example.presentation.main.MainActions
import com.example.presentation.main.MainState
import kotlinx.coroutines.flow.StateFlow

/**
 * This class acts as a concrete implementation of the contract, wrapping the internal
 * ViewModel. It delegates all calls to the actual implementation.
 */
class ContractImplementation internal constructor(
  private val contractViewModel : ContractViewModel
): ContractObservabilityApi {

  /**
   * Delegates the state access to the wrapped contract.
   */
  override val state: StateFlow<MainState>
    get() = contractViewModel.state

  /**
   * Delegates event handling to the wrapped contract.
   */
  override fun onEvent(event: MainActions) {
    contractViewModel.onEvent(event)
  }

}
