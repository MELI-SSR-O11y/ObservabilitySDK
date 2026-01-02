package com.example.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "metadata",
    indices = [Index("id"), Index("pkIncident")],
    foreignKeys = [
        ForeignKey(
            entity = IncidentTrackerEntity::class,
            parentColumns = ["id"],
            childColumns = ["pkIncident"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MetadataEntity(
  @PrimaryKey val id : String,
  val key : String,
  val value : String,
  val pkIncident : String,
)