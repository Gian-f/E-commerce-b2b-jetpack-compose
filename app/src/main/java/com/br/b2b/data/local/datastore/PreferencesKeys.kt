package com.br.b2b.data.local.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val SEARCH_HISTORY = stringPreferencesKey("search_history")
    val TOKEN = stringPreferencesKey("token")
}