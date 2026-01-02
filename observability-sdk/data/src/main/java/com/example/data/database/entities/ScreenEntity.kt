package com.example.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "screen", indices = [Index("id")])
data class ScreenEntity(
  @PrimaryKey val id : String,
  val name : String,
)