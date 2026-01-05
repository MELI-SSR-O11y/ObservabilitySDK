package com.example.domain.util

sealed interface DataError {
  enum class Remote: DataError {
    REQUEST_TIMEOUT,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    REDIRECTION,
    CLIENT_ERROR,
    UNKNOWN
  }

  enum class Connection: DataError {
    NOT_CONNECTED
  }
}