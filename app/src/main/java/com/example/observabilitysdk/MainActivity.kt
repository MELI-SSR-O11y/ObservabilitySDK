package com.example.observabilitysdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.observabilitysdk.ui.theme.ObservabilitySDKTheme
import com.example.presentation.SumaTwoTest

class MainActivity: ComponentActivity() {
  override fun onCreate(savedInstanceState : Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ObservabilitySDKTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Greeting(
            name = "TEST", modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }
}

@Composable
fun Greeting(name : String, modifier : Modifier = Modifier) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Button(onClick = { SumaTwoTest().twoNumbers(1,2) }, ) {
      Text(text = name, modifier = modifier.align(Alignment.CenterVertically), textAlign = TextAlign.Center)
    }
  }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  ObservabilitySDKTheme {
    Greeting("Android")
  }
}