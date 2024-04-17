package com.br.jetpacktest.domain.di

import com.br.jetpacktest.data.remote.infra.ApiServiceFactory
import com.br.jetpacktest.data.remote.service.ApiService
import com.br.jetpacktest.domain.repository.AuthRepository
import com.br.jetpacktest.domain.repository.AuthRepositoryImpl
import com.br.jetpacktest.ui.viewmodel.ThemeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    
    @Provides
    @Singleton
    fun provideThemeViewModel(): ThemeViewModel {
        return ThemeViewModel()
    }


    @Provides
    @Singleton
    fun provideAuthRepository(
        service: ApiService,
    ): AuthRepository {
        return AuthRepositoryImpl(service)
    }


    @Provides
    fun provideApiService(): ApiService {
        return ApiServiceFactory.createService()
    }
}