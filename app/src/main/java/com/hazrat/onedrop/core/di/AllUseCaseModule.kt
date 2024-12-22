package com.hazrat.onedrop.core.di

import com.hazrat.onedrop.core.domain.usecase.AllUseCases
import com.hazrat.onedrop.core.domain.usecase.CityValidation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AllUseCaseModule {


    @Provides
    fun provideAllUseCases(
        cityValidation: CityValidation
    ): AllUseCases {
        return AllUseCases(
            cityValidation = cityValidation
        )
    }

    @Provides
    fun provideCityValidation(): CityValidation {
        return CityValidation()
    }
}