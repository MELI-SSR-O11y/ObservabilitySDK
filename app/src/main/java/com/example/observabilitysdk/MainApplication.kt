package com.example.observabilitysdk

import android.app.Application
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.presentation.di.presentationModule
import io.kotzilla.sdk.analytics.koin.analytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@MainApplication)
      analytics()
      modules(
        dataModule, domainModule, presentationModule
      )
    }
  }
}