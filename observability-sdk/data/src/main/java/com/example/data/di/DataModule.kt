package com.example.data.di

import com.example.data.database.MeliDatabase
import com.example.data.repository.IncidentTrackerRepositoryImpl
import com.example.data.repository.ScreenRepositoryImpl
import com.example.domain.repositories.IncidentTrackerRepository
import com.example.domain.repositories.ScreenRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {

    // Database
    single {
        MeliDatabase.getInstance(androidContext())
    }

    // DAOs
    single { get<MeliDatabase>().screensDao() }
    single { get<MeliDatabase>().incidentDao() }
    single { get<MeliDatabase>().metadataDao() }

    singleOf(::IncidentTrackerRepositoryImpl) bind IncidentTrackerRepository::class
    singleOf(::ScreenRepositoryImpl) bind ScreenRepository::class
}