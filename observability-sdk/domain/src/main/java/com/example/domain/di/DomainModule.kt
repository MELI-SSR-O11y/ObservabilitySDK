package com.example.domain.di

import com.example.domain.usecases.FilterDataUseCase
import com.example.domain.usecases.GetAllScreensUseCase
import com.example.domain.usecases.InsertIncidentTrackerUseCase
import com.example.domain.usecases.InsertScreenUseCase
import com.example.domain.usecases.RollbackFromRemoteUseCase
import com.example.domain.usecases.SyncToRemoteUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {

    singleOf(::InsertIncidentTrackerUseCase)
    singleOf(::InsertScreenUseCase)
    singleOf(::GetAllScreensUseCase)
    singleOf(::SyncToRemoteUseCase)
    singleOf(::FilterDataUseCase)
    singleOf(::RollbackFromRemoteUseCase)

}