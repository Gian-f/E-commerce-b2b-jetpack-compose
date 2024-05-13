package com.br.b2b.domain.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.br.b2b.data.local.AppDatabase
import com.br.b2b.data.local.dao.CartItemDao
import com.br.b2b.data.local.dao.CategoryDao
import com.br.b2b.data.local.dao.ProductDao
import com.br.b2b.data.local.dao.UserDao
import com.br.b2b.data.local.datastore.DataStoreManager
import com.br.b2b.data.local.datastore.PreferencesKeys
import com.br.b2b.data.remote.infra.ApiServiceFactory
import com.br.b2b.data.remote.service.ApiService
import com.br.b2b.domain.repository.AuthRepository
import com.br.b2b.domain.repository.AuthRepositoryImpl
import com.br.b2b.domain.repository.CartItemRepository
import com.br.b2b.domain.repository.CartItemRepositoryImpl
import com.br.b2b.domain.repository.StoreRepository
import com.br.b2b.domain.repository.StoreRepositoryImpl
import com.br.b2b.domain.repository.UserRepositoryImpl
import com.br.b2b.ui.viewmodel.CartItemViewModel
import com.br.b2b.ui.viewmodel.LoginViewModel
import com.br.b2b.ui.viewmodel.SignUpViewModel
import com.br.b2b.ui.viewmodel.StoreViewModel
import com.br.b2b.ui.viewmodel.ThemeViewModel
import com.br.b2b.ui.viewmodel.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideThemeViewModel(): ThemeViewModel {
        return ThemeViewModel()
    }


    @Provides
    @Singleton
    fun provideSignUpViewModel(
        authRepository: AuthRepositoryImpl,
        userViewModel: UserViewModel
    ): SignUpViewModel {
        return SignUpViewModel(authRepository, userViewModel)
    }

    @Provides
    @Singleton
    fun provideUserViewModel(userRepository: UserRepositoryImpl): UserViewModel {
        return UserViewModel(userRepository)
    }

    @Provides
    @Singleton
    fun provideCartItemViewModel(cartItemRepository: CartItemRepositoryImpl): CartItemViewModel {
        return CartItemViewModel(cartItemRepository)
    }

    @Provides
    @Singleton
    fun provideLoginViewModel(
        authRepository: AuthRepositoryImpl,
        dataStoreManager: DataStoreManager,
        userViewModel: UserViewModel
    ): LoginViewModel {
        return LoginViewModel(authRepository, dataStoreManager, userViewModel)
    }

    @Provides
    @Singleton
    fun provideStoreViewModel(
        storeRepositoryImpl: StoreRepositoryImpl,
        cartItemRepository: CartItemRepositoryImpl,
        dataStoreManager: DataStoreManager
    ): StoreViewModel {
        return StoreViewModel(storeRepositoryImpl, cartItemRepository, dataStoreManager)
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
    fun provideCartItemRepository(
        dao: CartItemDao
    ): CartItemRepository {
        return CartItemRepositoryImpl(dao)
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
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(
                SharedPreferencesMigration(
                    appContext,
                    PreferencesKeys.SEARCH_HISTORY.name
                )
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(PreferencesKeys.SEARCH_HISTORY.name) }
        )
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "b2bDatabase.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideCartItemDao(database: AppDatabase): CartItemDao {
        return database.cartItemDao()
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