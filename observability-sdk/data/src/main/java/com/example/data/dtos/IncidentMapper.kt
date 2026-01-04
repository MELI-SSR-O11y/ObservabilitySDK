package com.example.data.dtos

import com.example.data.database.entities.IncidentTrackerEntity
import com.example.domain.models.IncidentTracker
import com.example.domain.models.Metadata
import com.example.domain.util.EIncidentSeverity

fun IncidentTrackerEntity.toIncidentTracker(metadata: List<Metadata>): IncidentTracker {
    return IncidentTracker(
        id = this.id,
        errorCode = this.errorCode,
        message = this.message,
        severity = EIncidentSeverity.valueOf(this.severity),
        screenId = this.pkScreen,
        metadata = metadata,
        isSync = this.isSync,
        timestamp = this.timestamp
    )
}

fun IncidentTracker.toEntity(screenId: String? = null): IncidentTrackerEntity {
    return IncidentTrackerEntity(
        id = this.id,
        errorCode = this.errorCode,
        message = this.message,
        severity = this.severity.name,
        pkScreen = screenId ?: this.screenId,
        isSync = false,
        timestamp = this.timestamp
    )
}
