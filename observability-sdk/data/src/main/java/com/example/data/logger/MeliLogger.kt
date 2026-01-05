package com.example.data.logger

import co.touchlab.kermit.Logger
import com.example.data.BuildConfig
import com.example.domain.logger.IMeliLogger

class MeliLogger : IMeliLogger {
  override fun debug(message : String) {
    when (BuildConfig.BUILD_TYPE) {
      "debug", "dev" -> Logger.d(message)
    }
  }

  override fun info(message : String) {
    when (BuildConfig.BUILD_TYPE) {
      "debug", "dev", "qa" -> Logger.i(message)
    }
  }

  override fun warn(message : String) {
    when (BuildConfig.BUILD_TYPE) {
      "debug", "dev", "qa" -> Logger.w(message)
    }
  }

  override fun error(message : String, throwable : Throwable?) {
    when (BuildConfig.BUILD_TYPE) {
      "debug", "dev", "qa" -> Logger.e(message, throwable)
    }
  }

  override fun critical(
    location : String,
    message : String,
    throwable : Throwable?,
  ) {
    when (BuildConfig.BUILD_TYPE) {
      "debug", "dev", "qa" -> Logger.v(message, throwable, location)
    }
  }
}