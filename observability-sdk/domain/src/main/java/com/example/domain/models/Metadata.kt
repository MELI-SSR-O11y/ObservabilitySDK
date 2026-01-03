@file:OptIn(ExperimentalUuidApi::class)

package com.example.domain.models

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
  val id : String = Uuid.random().toString(),
  val key : String,
  val value : String,
  val isSync : Boolean = false
)
