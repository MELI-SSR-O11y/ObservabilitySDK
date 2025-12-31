package com.example.data.networking

import com.example.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url

suspend inline fun <reified HttpResponse: Any> HttpClient.doPost(
  route: String,
  body: String? = null,
  crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<HttpResponse> {
  return doRequest(
    execute = {
      post {
        url(/*BuildConfig.BASE_URL*/"http://127.0.0.1:8080/"+route)
        setBody(body)
        builder()
      }
    },
    handleResponse = { it.body() }
  )
}
