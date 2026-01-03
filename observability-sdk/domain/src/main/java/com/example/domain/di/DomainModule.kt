package com.example.domain.di

import com.example.domain.repositories.IncidentTrackerRepository
import com.example.domain.repositories.ScreenRepository
import com.example.domain.usecases.GetAllScreensUseCase
import com.example.domain.usecases.InsertIncidentTrackerUseCase
import com.example.domain.usecases.InsertScreenUseCase
import org.koin.dsl.module

val domainModule = module {

    single {
        InsertIncidentTrackerUseCase(
            incidentTrackerRepository = get<IncidentTrackerRepository>()
        )
    }

    single {
        InsertScreenUseCase(
            screenRepository = get<ScreenRepository>()
        )
    }

    single {
        GetAllScreensUseCase(
            screenRepository = get<ScreenRepository>()
        )
    }
    
}