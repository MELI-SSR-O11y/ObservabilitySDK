package com.example.presentation.main

import com.example.domain.models.Screen

data class MainScreenAction(
  val screens: List<Screen> = emptyList(),
  val isLoading: Boolean = true,
  val error: String? = null,

  val incidentsQuantity: Int = 0,
  val screensQuantity: Int = 0,
  val debugSeverityQuantity: Int = 0,
  val infoSeverityQuantity: Int = 0,
  val warningSeverityQuantity: Int = 0,
  val errorSeverityQuantity: Int = 0,
  val criticalSeverityQuantity: Int = 0,
)