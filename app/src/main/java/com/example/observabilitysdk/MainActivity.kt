package com.example.observabilitysdk

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.models.IncidentTracker
import com.example.domain.models.TimeFilter
import com.example.domain.util.EIncidentSeverity
import com.example.observabilitysdk.ui.FilterDropDown
import com.example.observabilitysdk.ui.theme.ObservabilitySDKTheme
import com.example.presentation.main.ContractViewModel
import com.example.presentation.main.MainActions
import com.example.presentation.main.MainState
import java.time.Duration
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ObservabilitySDKTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val sdk: ContractViewModel = koinViewModel()
    val state by sdk.state.collectAsStateWithLifecycle()
    val onEvent = sdk::onEvent
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.height(4.dp).fillMaxWidth())
        }

        // Filter Section (Adaptive)
        if (isLandscape) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterDropDown(
                    label = "Screen",
                    items = state.screens,
                    selectedItem = state.screens.find { it.id == state.activeFilter.screenId },
                    onItemSelected = { onEvent(MainActions.FilterByScreen(it?.id)) },
                    itemToString = { it.name },
                    modifier = Modifier.weight(1f)
                )
                FilterDropDown(
                    label = "Severity",
                    items = EIncidentSeverity.entries.toList(),
                    selectedItem = state.activeFilter.severity,
                    onItemSelected = { onEvent(MainActions.FilterBySeverity(it)) },
                    itemToString = { it.name },
                    modifier = Modifier.weight(1f)
                )
                FilterDropDown(
                    label = "Time",
                    items = TimeFilter.allFilters(),
                    selectedItem = state.activeFilter.timeFilter,
                    onItemSelected = { onEvent(MainActions.FilterByTime(it ?: TimeFilter.None)) },
                    itemToString = { it.displayName },
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterDropDown(
                    label = "Screen",
                    items = state.screens,
                    selectedItem = state.screens.find { it.id == state.activeFilter.screenId },
                    onItemSelected = { onEvent(MainActions.FilterByScreen(it?.id)) },
                    itemToString = { it.name },
                    modifier = Modifier.weight(1f)
                )
                FilterDropDown(
                    label = "Severity",
                    items = EIncidentSeverity.entries.toList(),
                    selectedItem = state.activeFilter.severity,
                    onItemSelected = { onEvent(MainActions.FilterBySeverity(it)) },
                    itemToString = { it.name },
                    modifier = Modifier.weight(1f)
                )
            }
            FilterDropDown(
                label = "Time",
                items = TimeFilter.allFilters(),
                selectedItem = state.activeFilter.timeFilter,
                onItemSelected = { onEvent(MainActions.FilterByTime(it ?: TimeFilter.None)) },
                itemToString = { it.displayName },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Screens: ${state.screensQuantity}")
        Text(text = "Incidents: ${state.incidentsQuantity}")
        Spacer(modifier = Modifier.height(16.dp))

        // Charts Section (Adaptive)
        if (isLandscape) {
            Row(Modifier.fillMaxWidth().height(300.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.weight(1f)) { SeverityPieChart(state) }
                Box(modifier = Modifier.weight(1f)) { IncidentTimeSeriesChart(state) }
            }
        } else {
            SeverityPieChart(state)
            Spacer(modifier = Modifier.height(24.dp))
            IncidentTimeSeriesChart(state)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { onEvent(MainActions.InsertScreen("Pantalla 2")) }) {
            Text("Add Screen")
        }
        Button(onClick = {
            onEvent(
                MainActions.InsertIncident(
                    IncidentTracker(
                        errorCode = 500,
                        message = "Servidor",
                        severity = EIncidentSeverity.DEBUG,
                        pkScreen = "081b05e6-7709-40b8-bbed-7b05958f6432",
                        timestamp = System.currentTimeMillis(),
                        metadata = listOf(com.example.domain.models.Metadata(key = "key", value = "value"))
                    ), "Pantalla 2"
                )
            )
        }) {
            Text("Add Event")
        }
        Button(onClick = { onEvent(MainActions.SyncToRemote) }) {
            Text("Sync To Remote")
        }
    }
}

private data class PieChartData(val label: String, val count: Int, val color: Color)

@Composable
private fun SeverityPieChart(state: MainState, modifier: Modifier = Modifier) {
    val severityData = listOfNotNull(
        if (state.debugSeverityQuantity > 0) PieChartData("Debug", state.debugSeverityQuantity, Color.Gray) else null,
        if (state.infoSeverityQuantity > 0) PieChartData("Info", state.infoSeverityQuantity, Color.Blue) else null,
        if (state.warningSeverityQuantity > 0) PieChartData("Warning", state.warningSeverityQuantity, Color(0xFFFFA500)) else null, // Naranja
        if (state.errorSeverityQuantity > 0) PieChartData("Error", state.errorSeverityQuantity, Color.Red) else null,
        if (state.criticalSeverityQuantity > 0) PieChartData("Critical", state.criticalSeverityQuantity, Color(0xFF8B0000)) else null // Rojo Oscuro
    )

    if (severityData.isEmpty()) {
        Text("No incident data for pie chart.", modifier = modifier.padding(vertical = 32.dp))
        return
    }

    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Incident Severity Distribution", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val total = severityData.sumOf { it.count }.toFloat()
                    var startAngle = -90f
                    severityData.forEach { data ->
                        val sweepAngle = (data.count / total) * 360f
                        drawArc(
                            color = data.color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true
                        )
                        startAngle += sweepAngle
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                severityData.forEach { data ->
                    LegendItem(label = "${data.label} (${data.count})", color = data.color)
                }
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun IncidentTimeSeriesChart(state: MainState, modifier: Modifier = Modifier) {
    val filter = state.activeFilter
    val incidentsToDisplay = state.screens
        // Apply screen filter first if a screen is selected
        .filter { screen -> filter.screenId == null || screen.id == filter.screenId }
        // Flatten to a list of all incidents from the filtered screens
        .flatMap { it.incidentTrackers }
        // Then, filter these incidents by severity and time
        .filter { incident ->
            val severityMatch = filter.severity == null || incident.severity == filter.severity
            val timeLimit = filter.timeFilter.durationMillis.takeIf { it > 0 }?.let {
                System.currentTimeMillis() - it
            }
            val timeMatch = timeLimit == null || incident.timestamp >= timeLimit
            severityMatch && timeMatch
        }

    if (incidentsToDisplay.size < 2) {
        Text("Not enough data for time series chart with current filters.", modifier = modifier.padding(vertical = 32.dp))
        return
    }

    val timestamps = incidentsToDisplay.map { it.timestamp }
    val minTimestamp = timestamps.minOrNull() ?: return
    val maxTimestamp = timestamps.maxOrNull() ?: return
    val duration = Duration.ofMillis(maxTimestamp - minTimestamp)

    val (formatter, incidentsByTime) = when {
        duration.toMinutes() < 120 -> DateTimeFormatter.ofPattern("HH:mm") to incidentsToDisplay.groupBy { Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime().truncatedTo(ChronoUnit.MINUTES) }.mapValues { it.value.size }.toSortedMap()
        duration.toHours() < 48 -> DateTimeFormatter.ofPattern("d / HH:00") to incidentsToDisplay.groupBy { Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime().truncatedTo(ChronoUnit.HOURS) }.mapValues { it.value.size }.toSortedMap()
        duration.toDays() < 60 -> DateTimeFormatter.ofPattern("MMM d") to incidentsToDisplay.groupBy { Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDate() }.mapValues { it.value.size }.toSortedMap()
        else -> DateTimeFormatter.ofPattern("MMM yyyy") to incidentsToDisplay.groupBy { YearMonth.from(Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDate()) }.mapValues { it.value.size }.toSortedMap()
    }

    if (incidentsByTime.size < 2) {
        Text("Data points are not distinct enough to draw a line chart.", modifier = modifier.padding(vertical = 32.dp))
        return
    }

    val dataPoints = incidentsByTime.entries.toList()
    val maxIncidents = incidentsByTime.values.maxOrNull() ?: 1
    val lineChartColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val yAxisLabelWidth = 40.dp

    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Incidents Over Time", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            // Y-Axis Labels
            Column(
                modifier = Modifier.width(yAxisLabelWidth).fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(maxIncidents.toString(), style = MaterialTheme.typography.bodySmall)
                Text("0", style = MaterialTheme.typography.bodySmall)
            }

            // Chart Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val chartWidth = size.width
                val chartHeight = size.height

                // Draw X and Y axes
                drawLine(color = axisColor, start = Offset(0f, chartHeight), end = Offset(chartWidth, chartHeight), strokeWidth = 2f)
                drawLine(color = axisColor, start = Offset(0f, 0f), end = Offset(0f, chartHeight), strokeWidth = 2f)

                val xStep = if (dataPoints.size > 1) chartWidth / (dataPoints.size - 1) else 0f
                val yRatio = if (maxIncidents > 0) chartHeight / maxIncidents else 0f

                // Draw the line and points
                for (i in 0 until dataPoints.size - 1) {
                    val p1 = dataPoints[i]
                    val p2 = dataPoints[i + 1]
                    val x1 = i * xStep
                    val y1 = chartHeight - (p1.value * yRatio)
                    val x2 = (i + 1) * xStep
                    val y2 = chartHeight - (p2.value * yRatio)
                    drawLine(color = lineChartColor, start = Offset(x1, y1), end = Offset(x2, y2), strokeWidth = 4f)
                }
                dataPoints.forEachIndexed { index, entry ->
                    val x = index * xStep
                    val y = chartHeight - (entry.value * yRatio)
                    drawCircle(color = lineChartColor, radius = 8f, center = Offset(x, y))
                }
            }
        }

        // X-Axis Labels
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = yAxisLabelWidth),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val firstLabel = formatter.format(dataPoints.first().key as Temporal)
            val lastLabel = formatter.format(dataPoints.last().key as Temporal)

            Text(firstLabel, style = MaterialTheme.typography.bodySmall)
            if (dataPoints.size > 2) {
                val middleIndex = dataPoints.size / 2
                val middleLabel = formatter.format(dataPoints[middleIndex].key as Temporal)
                Text(middleLabel, style = MaterialTheme.typography.bodySmall)
            }
            Text(lastLabel, style = MaterialTheme.typography.bodySmall)
        }
    }
}
