package com.br.b2b.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    suspend fun getToken(): String {
        return dataStore.data.map { it[PreferencesKeys.TOKEN] ?: "" }.first()
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOKEN] = token
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it.remove(PreferencesKeys.TOKEN)
        }
    }

    suspend fun clearSearchHistory() {
        dataStore.edit {
            it.remove(PreferencesKeys.SEARCH_HISTORY)
        }
    }

    suspend fun getSearchHistory(): List<String> {
        return dataStore.data.first()[PreferencesKeys.SEARCH_HISTORY]?.split(",") ?: emptyList()
    }

    suspend fun updateSearchHistory(searchTerm: String, onSuccess: () -> Unit) {
        if (searchTerm.isNotEmpty()) {
            val currentHistory = getSearchHistory()
            val updatedHistory = (listOf(searchTerm) + currentHistory).distinct().take(5)
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.SEARCH_HISTORY] = updatedHistory.joinToString(",")
            }
            onSuccess.invoke()
        }
    }
}