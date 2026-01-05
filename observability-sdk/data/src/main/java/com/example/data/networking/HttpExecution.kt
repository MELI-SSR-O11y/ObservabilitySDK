package com.example.data.networking

import com.example.domain.logger.IMeliLogger
import com.example.domain.util.DataError
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException

suspend fun <T> doRequest(
  logger : IMeliLogger,
  execute : suspend () -> HttpResponse,
  handleResponse : suspend (HttpResponse) -> Result<T>,
) : Result<T> {

  return try {
    val response = execute()
    handleResponse(response)
  } catch(e : UnknownHostException) {
    logger.warn("UnknownHostException")
    Result.failure(Failure(DataError.Remote.NO_INTERNET, e.message ?: "Unknown host exception"))
  } catch(e : UnresolvedAddressException) {
    logger.error("UnresolvedAddressException", e)
    Result.failure(Failure(DataError.Remote.NO_INTERNET, e.message ?: "Unknown host exception"))
  } catch(e : ConnectException) {
    logger.warn("ConnectException")
    Result.failure(Failure(DataError.Remote.NO_INTERNET, e.localizedMessage ?: "Unknown host exception"))
  } catch(e : SocketTimeoutException) {
    logger.error("SocketTimeoutException", e)
    Result.failure(Failure(DataError.Remote.REQUEST_TIMEOUT, e.message ?: "Unknown host exception"))
  } catch(e : HttpRequestTimeoutException) {
    logger.error("HttpRequestTimeoutException", e)
    Result.failure(Failure(DataError.Remote.REQUEST_TIMEOUT, e.message ?: "Unknown host exception"))
  } catch(e : SerializationException) {
    logger.critical("SerializationException", e.message ?: "Error de serializacion", e)
    Result.failure(Failure(DataError.Remote.SERIALIZATION, e.message ?: "Unknown host exception"))
  } catch(e : Exception) {
    logger.error("Exception", e)
    coroutineContext.ensureActive()
    Result.failure(Failure(DataError.Remote.UNKNOWN, e.message ?: "Unknown host exception"))
  }
}

class Failure(val error: DataError, message: String): Exception()