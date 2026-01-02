package com.example.data.di

import com.example.data.database.MeliDatabase
import com.example.data.repository.IncidentTrackerRepositoryImpl
import com.example.domain.repositories.IncidentTrackerRepository
import org.koin.android.ext.koin.androidContext
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

    // Repositories
    single<IncidentTrackerRepository> {
        IncidentTrackerRepositoryImpl(incidentDao = get())
    }
}