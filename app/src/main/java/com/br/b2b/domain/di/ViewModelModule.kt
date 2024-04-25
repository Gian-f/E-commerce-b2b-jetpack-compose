package com.br.b2b.domain.di

import com.br.b2b.data.remote.infra.ApiServiceFactory
import com.br.b2b.data.remote.service.ApiService
import com.br.b2b.domain.repository.AuthRepository
import com.br.b2b.domain.repository.AuthRepositoryImpl
import com.br.b2b.domain.repository.StoreRepository
import com.br.b2b.domain.repository.StoreRepositoryImpl
import com.br.b2b.ui.viewmodel.LoginViewModel
import com.br.b2b.ui.viewmodel.SignUpViewModel
import com.br.b2b.ui.viewmodel.StoreViewModel
import com.br.b2b.ui.viewmodel.ThemeViewModel
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
    fun provideSignUpViewModel(authRepository: AuthRepositoryImpl): SignUpViewModel {
        return SignUpViewModel(authRepository)
    }

    @Provides
    @Singleton
    fun provideLoginViewModel(authRepository: AuthRepositoryImpl): LoginViewModel {
        return LoginViewModel(authRepository)
    }

    @Provides
    @Singleton
    fun provideStoreViewModel(storeRepositoryImpl: StoreRepositoryImpl): StoreViewModel {
        return StoreViewModel(storeRepositoryImpl)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(
        service: ApiService,
    ): AuthRepository {
        return AuthRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideStoreRepository(
        service: ApiService,
    ): StoreRepository {
        return StoreRepositoryImpl(service)
    }


    @Provides
    fun provideApiService(): ApiService {
        return ApiServiceFactory.createService()
    }
}