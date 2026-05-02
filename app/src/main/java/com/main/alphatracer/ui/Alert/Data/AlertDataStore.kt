package com.main.alphatracer.ui.Alert.Data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.main.alphatracer.model.AlertRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "alert_rules")

class AlertDataStore(private val context: Context) {

    private val RULES_KEY = stringPreferencesKey("rules_list")

    suspend fun saveRules(rules: List<AlertRule>) {
        val json = Json.encodeToString(rules)
        context.dataStore.edit { prefs ->
            prefs[RULES_KEY] = json
        }
    }

    fun getRulesFlow(): Flow<List<AlertRule>> = context.dataStore.data.map { prefs ->
        val json = prefs[RULES_KEY] ?: return@map emptyList()
        Json.decodeFromString(json)
    }

    suspend fun getRules(): List<AlertRule> = getRulesFlow().first()
}