package com.example.data.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.data.database.MeliDatabase
import com.example.data.database.migrations.MIGRATION_1_2
import com.example.data.database.migrations.MIGRATION_2_3
import com.example.data.logger.MeliLogger
import com.example.data.networking.HttpClientFactory
import com.example.data.repository.IncidentTrackerRepositoryImpl
import com.example.data.repository.ScreenRepositoryImpl
import com.example.data.service.ObservabilityService
import com.example.domain.logger.IMeliLogger
import com.example.domain.repositories.IncidentTrackerRepository
import com.example.domain.repositories.ScreenRepository
import com.example.domain.service.IObservabilityService
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module(createdAtStart = true) {

    // Database
    single<MeliDatabase> {
        Room.databaseBuilder(
            androidContext(), MeliDatabase::class.java, "MELI_Observability.db"
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).setDriver(BundledSQLiteDriver()).build()
    }

    // DAOs
    single { get<MeliDatabase>().screensDao() }
    single { get<MeliDatabase>().incidentDao() }
    single { get<MeliDatabase>().metadataDao() }

    // Logger
    single { MeliLogger() } bind IMeliLogger::class

    // Networking
    single { OkHttp.create() } bind HttpClientEngine::class
    single {
        HttpClientFactory(get<IMeliLogger>()).create(get())
    } bind HttpClient::class
    singleOf( ::ObservabilityService) bind IObservabilityService::class

    // Repositories
    singleOf(::IncidentTrackerRepositoryImpl) bind IncidentTrackerRepository::class
    singleOf(::ScreenRepositoryImpl) bind ScreenRepository::class
}