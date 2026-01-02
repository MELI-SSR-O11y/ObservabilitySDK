package com.example.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "incident_tracker",
    indices = [Index("id"), Index("pkScreen")],
    foreignKeys = [
        ForeignKey(
            entity = ScreenEntity::class,
            parentColumns = ["id"],
            childColumns = ["pkScreen"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IncidentTrackerEntity(
  @PrimaryKey val id : String,
  val errorCode : Int,
  val message : String,
  val severity : String,
  val pkScreen : String,
  val isSync : Boolean = false
)