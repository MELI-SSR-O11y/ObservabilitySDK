package com.example.domain.models

import com.example.domain.util.EIncidentSeverity

/**
 * Data class to hold all active filters for incidents.
 */
data class IncidentFilter(
    val screenId: String? = null,
    val timeFilter: TimeFilter = TimeFilter.None,
    val severity: EIncidentSeverity? = null
)

/**
 * Sealed class representing the different time filter options.
 */
sealed class TimeFilter(val durationMinutes: Long) {
    object None : TimeFilter(0)
    object LastMinute : TimeFilter(1*1000*60)
    object Last5Minutes : TimeFilter(5*1000*60)
    object Last30Minutes : TimeFilter(30*1000*60)
    object Last12Hours : TimeFilter(12*1000*60*60)
    object Last24Hours : TimeFilter(24*1000*60*60)
}