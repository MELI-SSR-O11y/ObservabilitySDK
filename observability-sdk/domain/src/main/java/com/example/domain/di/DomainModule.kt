package com.example.domain.di

import com.example.domain.usecases.InsertIncidentTrackerUseCase
import org.koin.dsl.module

val domainModule = module {
    
    // Use Cases
    factory { InsertIncidentTrackerUseCase(incidentTrackerRepository = get()) }
    
}