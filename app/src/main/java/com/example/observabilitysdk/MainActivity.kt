package com.example.observabilitysdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.models.IncidentTracker
import com.example.observabilitysdk.ui.theme.ObservabilitySDKTheme
import com.example.presentation.main.MainActions
import com.example.presentation.main.ContractViewModel
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
fun MainScreen(modifier: Modifier = Modifier, sdk: ContractViewModel = koinViewModel()) {

    val state by sdk.state.collectAsStateWithLifecycle()
    val onEvent = sdk::onEvent

    Column(modifier = modifier) {
        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.height(4.dp).fillMaxWidth())
        }
        Text(text = "Screens: ${state.screensQuantity}")
        Text(text = "Incidents: ${state.incidentsQuantity}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Debug: ${state.debugSeverityQuantity}")
        Text(text = "Info: ${state.infoSeverityQuantity}")
        Text(text = "Warning: ${state.warningSeverityQuantity}")
        Text(text = "Error: ${state.errorSeverityQuantity}")
        Text(text = "Critical: ${state.criticalSeverityQuantity}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Is Sync: ${state.isSync}")
        Text(text = "Is Loading: ${state.isLoading}")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onEvent(MainActions.InsertScreen("New Screen")) }) {
            Text("Add Screen")
        }
        Button(onClick = {
            onEvent(
                MainActions.InsertIncident(
                    IncidentTracker(
                        errorCode = 500,
                        message = "Servidor",
                        severity = com.example.domain.util.EIncidentSeverity.DEBUG,
                        pkScreen = "fb852b03-47d6-431b-86e6-5eebefe98aa7",
                        metadata = listOf(
                            com.example.domain.models.Metadata(
                                key = "key", value = "value"
                            )
                        )
                    )
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
