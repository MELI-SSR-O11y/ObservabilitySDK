package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Screen(
  val id: String,
  val name: String,
  val isSync: Boolean = false,
  val incidentTrackers: List<IncidentTracker> = listOf()
)
