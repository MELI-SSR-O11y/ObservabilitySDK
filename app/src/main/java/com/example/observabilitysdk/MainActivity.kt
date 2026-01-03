package com.example.observabilitysdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.models.IncidentTracker
import com.example.observabilitysdk.ui.theme.ObservabilitySDKTheme
import com.example.presentation.main.ContractViewModel
import com.example.presentation.main.MainActions
import com.example.presentation.main.MainState
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

    Column(modifier = modifier.padding(16.dp)) {
        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.height(4.dp).fillMaxWidth())
        }
        Text(text = "Screens: ${state.screensQuantity}")
        Text(text = "Incidents: ${state.incidentsQuantity}")
        Spacer(modifier = Modifier.height(16.dp))

        // Gráfica de Torta
        SeverityPieChart(state)

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
                        severity = com.example.domain.util.EIncidentSeverity.ERROR,
                        pkScreen = "fb852b03-47d6-431b-86e6-5eebefe98aa7",
                        metadata = listOf(
                            com.example.domain.models.Metadata(
                                key = "key", value = "value"
                            )
                        )
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
        Text("No incident data to display.", modifier = modifier.padding(vertical = 32.dp))
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

            // Leyenda
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
