@file:OptIn(ExperimentalUuidApi::class)

package com.example.domain.models

import com.example.domain.util.EIncidentSeverity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class IncidentTracker(
  val id: String = Uuid.random().toString(),
  val errorCode: Int,
  val message: String,
  val severity: EIncidentSeverity,
  val screenId: String,
  val isSync: Boolean = false,
  val timestamp: Long,
  val metadata: List<Metadata>
)
