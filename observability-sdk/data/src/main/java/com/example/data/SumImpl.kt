package com.example.data

import android.util.Log
import com.example.data.logger.MeliLogger
import com.example.data.networking.HttpClientFactory
import com.example.data.service.ObservabilityService
import com.example.domain.ISumTest
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SumImpl: ISumTest {
  override fun twoNumbers(a : Int, b : Int) : Int {
    val logger = MeliLogger()
    val engine = OkHttp.create()
    val client = HttpClientFactory(logger).create(engine)
    val service = ObservabilityService(logger, client)
    CoroutineScope(IO).launch {
      service.addScreen("Pantalla 95").onSuccess { value ->
        value
        logger.debug("Success")
        logger.debug(value.toString())
        Log.e("Success", value.toString())
      }.onFailure { error->
        error as com.example.data.networking.Failure
        error.error
        error.message
        logger.debug("Failure")
        logger.debug(error.message ?: "Unknown error")
        Log.e("Failure", error.message ?: "Unknown error")
        error.message
      }
    }
    return a+b-1
  }

}