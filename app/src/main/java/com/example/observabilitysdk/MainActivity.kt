package com.example.observabilitysdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.observabilitysdk.ui.theme.ObservabilitySDKTheme
import com.example.presentation.main.MainActions
import com.example.presentation.main.MainViewModel
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
fun MainScreen(modifier: Modifier = Modifier, api: MainViewModel = koinViewModel()) {

    val state by api.state.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = "Screens: ${state.screensQuantity}")
            Text(text = "Incidents: ${state.incidentsQuantity}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Debug: ${state.debugSeverityQuantity}")
            Text(text = "Info: ${state.infoSeverityQuantity}")
            Text(text = "Warning: ${state.warningSeverityQuantity}")
            Text(text = "Error: ${state.errorSeverityQuantity}")
            Text(text = "Critical: ${state.criticalSeverityQuantity}")
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { api.onEvent(MainActions.InsertScreen("New Screen")) }) {
                Text("Add Screen")
            }
        }
    }
}
