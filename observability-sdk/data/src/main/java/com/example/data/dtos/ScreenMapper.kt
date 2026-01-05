package com.example.data.dtos

import com.example.data.database.entities.ScreenEntity
import com.example.domain.models.IncidentTracker
import com.example.domain.models.Screen

fun ScreenEntity.toScreen(incidentTrackers: List<IncidentTracker>): Screen {
    return Screen(
        id = this.id,
        name = this.name,
        incidentTrackers = incidentTrackers,
        isSync = this.isSync
    )
}

fun Screen.toEntity(): ScreenEntity {
    return ScreenEntity(
        id = this.id,
        name = this.name,
        isSync = true
    )
}
