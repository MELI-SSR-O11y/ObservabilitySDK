package com.example.domain.models

data class IncidentTracker(
  val errorCode: Int,
  val message: String,
  val severity: String,
  val metadata: List<Metadata>
)
