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
sealed class TimeFilter(val durationMillis: Long, val displayName: String) {
    object None : TimeFilter(0, "All Time")
    object LastMinute : TimeFilter(1 * 60 * 1000, "Last Minute")
    object Last5Minutes : TimeFilter(5 * 60 * 1000, "Last 5 Minutes")
    object Last30Minutes : TimeFilter(30 * 60 * 1000, "Last 30 Minutes")
    object Last12Hours : TimeFilter(12 * 60 * 60 * 1000, "Last 12 Hours")
    object Last24Hours : TimeFilter(24 * 60 * 60 * 1000, "Last 24 Hours")

    companion object {
        fun allFilters() = listOf(None, LastMinute, Last5Minutes, Last30Minutes, Last12Hours, Last24Hours)
    }
}