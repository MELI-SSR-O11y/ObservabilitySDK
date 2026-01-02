package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
  val id : String,
  val key : String,
  val value : String,
)
