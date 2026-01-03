package com.example.domain.di

import com.example.domain.usecases.GetAllScreensUseCase
import com.example.domain.usecases.InsertIncidentTrackerUseCase
import com.example.domain.usecases.InsertScreenUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { InsertIncidentTrackerUseCase(incidentTrackerRepository = get()) }
    factory { InsertScreenUseCase(screenRepository = get()) }
    factory { GetAllScreensUseCase(screenRepository = get()) }
    
}