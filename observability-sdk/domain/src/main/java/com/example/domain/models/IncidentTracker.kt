package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class IncidentTracker(
  val id: String,
  val errorCode: Int,
  val message: String,
  val severity: String,
  val pkScreen: String,
  val metadata: List<Metadata>
)
