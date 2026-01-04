package com.example.data.networking

import com.example.data.BuildConfig
import com.example.domain.logger.IMeliLogger
import com.example.domain.util.DataError
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified Body, reified HttpResponse: Any> HttpClient.doPost(
  route: String,
  body: Body? = null,
  logger: IMeliLogger,
  crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<HttpResponse> {
  val url = BuildConfig.BASE_URL+route
  logger.debug("MeliHttpClient::doPost -> $route, $body")
  return doRequest(
    execute = {
      post {
        url(url)
        setBody(body)
        builder()
      }
    }, logger = logger
  ) { response -> responseToResult(response, logger, route) }
}
suspend inline fun <reified HttpResponse: Any> HttpClient.doGet(
  route: String,
  logger: IMeliLogger,
  crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<HttpResponse> {
  val url = BuildConfig.BASE_URL+route
  logger.debug("MeliHttpClient::doGet -> $route")
  return doRequest(
    execute = {
      get {
        url(url)
        builder()
      }
    }, logger = logger
  ) { response -> responseToResult(response, logger, route) }
}

suspend inline fun <reified T> responseToResult(
  response : HttpResponse,
  logger : IMeliLogger,
  path : String,
) : Result<T> {
  return when(response.status.value) {
    in 200..299 -> try {
      logger.info("MeliHttClient::responseToResult::Success -> $path")
      Result.success(response.body<T>())
    } catch(e : NoTransformationFoundException) {
      logger.critical("MeliHttClient::responseToResult::Fail -> $path", e.message, e)
      logger.error("${response.status.value} - ${response.status.description}")
      Result.failure(Failure(DataError.Remote.SERIALIZATION, e.message))
    } catch(e : Exception) {
      logger.critical("MeliHttClient::responseToResult::Fail -> $path", e.message ?: "Error desconocido", e)
      logger.error("${response.status.value} - ${response.status.description}")
      Result.failure(Failure(DataError.Remote.UNKNOWN, e.message ?: "Error desconocido"))
    }

    in 300..399 -> {
      logger.error("MeliHttClient::responseToResult::Fail::300..399 -> $path")
      logger.error("${response.status.value} - ${response.status.description}")
      Result.failure(Exception(DataError.Remote.REDIRECTION.name))
    }
    in 400..499 -> {
      logger.error("MeliHttClient::responseToResult::Fail::400..499 -> $path")
      logger.error("${response.status.value} - ${response.status.description}")
      Result.failure(Exception(DataError.Remote.CLIENT_ERROR.name))
    }
    in 500..599 -> {
      logger.critical("MeliHttpClient::responseToResult::Fail::500..599", "Error del servidor")
      logger.error("${response.status.value} - ${response.status.description}")
      Result.failure(Exception(DataError.Remote.SERVER_ERROR.name))
    }
    else -> {
      logger.error("MeliHttClient::responseToResult::Fail -> $path")
      logger.error("${response.status.value} - ${response.status.description}")
      Result.failure(Exception(DataError.Connection.NOT_CONNECTED.name))
    }
  }
}
