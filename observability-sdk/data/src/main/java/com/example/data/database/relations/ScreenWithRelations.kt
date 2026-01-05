package com.example.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.data.database.entities.IncidentTrackerEntity
import com.example.data.database.entities.MetadataEntity
import com.example.data.database.entities.ScreenEntity

data class ScreenWithIncidents(
    @Embedded val screen: ScreenEntity,
    @Relation(
        parentColumn = "id",
        entity = IncidentTrackerEntity::class,
        entityColumn = "pkScreen"
    )
    val incidents: List<IncidentWithMetadata>
)

data class IncidentWithMetadata(
    @Embedded val incident: IncidentTrackerEntity,
    @Relation(
        parentColumn = "id",
        entity = MetadataEntity::class,
        entityColumn = "pkIncident"
    )
    val metadata: List<MetadataEntity>
)
