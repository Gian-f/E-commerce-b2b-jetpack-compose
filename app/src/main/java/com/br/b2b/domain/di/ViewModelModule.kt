package com.br.b2b.domain.di

import android.content.Context
import androidx.room.Room
import com.br.b2b.data.local.AppDatabase
import com.br.b2b.data.local.dao.CategoryDao
import com.br.b2b.data.local.dao.ProductDao
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
import dagger.hilt.android.qualifiers.ApplicationContext
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
        productDao: ProductDao,
        categoryDao: CategoryDao,
    ): StoreRepository {
        return StoreRepositoryImpl(service, productDao, categoryDao)
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "b2bDatabase.db"
        ).build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideApiService(): ApiService {
        return ApiServiceFactory.createService()
    }
}