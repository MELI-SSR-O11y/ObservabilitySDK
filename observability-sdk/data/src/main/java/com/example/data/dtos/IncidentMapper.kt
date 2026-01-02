package com.example.data.dtos

import com.example.data.database.entities.IncidentTrackerEntity
import com.example.domain.models.IncidentTracker
import com.example.domain.models.Metadata

fun IncidentTrackerEntity.toIncidentTracker(metadata: List<Metadata>): IncidentTracker {
    return IncidentTracker(
        id = this.id,
        errorCode = this.errorCode,
        message = this.message,
        severity = this.severity,
        pkScreen = this.pkScreen,
        metadata = metadata
    )
}

fun IncidentTracker.toEntity(): IncidentTrackerEntity {
    return IncidentTrackerEntity(
        id = this.id,
        errorCode = this.errorCode,
        message = this.message,
        severity = this.severity,
        pkScreen = this.pkScreen,
        isSync = true
    )
}
