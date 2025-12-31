package com.example.domain.logger

interface IMeliLogger {
  fun debug(message: String)
  fun info(message: String)
  fun warn(message: String)
  fun error(message: String, throwable: Throwable? = null)
  fun critical(location: String, message: String, throwable: Throwable? = null)
}