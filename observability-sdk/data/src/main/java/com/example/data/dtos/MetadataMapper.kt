package com.example.data.dtos

import com.example.data.database.entities.MetadataEntity
import com.example.domain.models.Metadata

fun MetadataEntity.toMetadata(): Metadata {
    return Metadata(
        id = this.id,
        key = this.key,
        value = this.value,
        isSync = this.isSync
    )
}

fun Metadata.toEntity(incidentId: String): MetadataEntity {
    return MetadataEntity(
        id = this.id,
        key = this.key,
        value = this.value,
        pkIncident = incidentId,
        isSync = false
    )
}
