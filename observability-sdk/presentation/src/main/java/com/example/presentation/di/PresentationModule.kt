package com.example.presentation.di

import com.example.presentation.ContractImplementation
import com.example.presentation.main.ContractObservabilityApi
import com.example.presentation.main.ContractViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val presentationModule = module {

    viewModelOf(::ContractViewModel)
    factoryOf(::ContractImplementation) bind ContractObservabilityApi::class
    
}