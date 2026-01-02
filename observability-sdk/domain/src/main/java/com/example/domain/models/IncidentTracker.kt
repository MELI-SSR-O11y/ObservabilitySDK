package com.example.domain.models

import com.example.domain.util.EIncidentSeverity
import kotlinx.serialization.Serializable

@Serializable
data class IncidentTracker(
  val id: String,
  val errorCode: Int,
  val message: String,
  val severity: EIncidentSeverity,
  val pkScreen: String,
  val metadata: List<Metadata>
)
