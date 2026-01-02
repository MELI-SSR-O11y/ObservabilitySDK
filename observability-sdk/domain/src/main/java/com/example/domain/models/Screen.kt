package com.example.domain.models

data class Screen(
  val id: String,
  val name: String,
  val incidentTrackers: List<IncidentTracker>
)
