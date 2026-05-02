package com.main.alphatracer.ui.Alert.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.main.alphatracer.model.AlertRule
import com.main.alphatracer.ui.Alert.Data.AlertDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class AlertViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = AlertDataStore(getApplication())

    fun saveAlertRule(rule: AlertRule) {
        viewModelScope.launch {
            val current = dataStore.getRules()
            val updated = current + rule
            dataStore.saveRules(updated)
        }
    }

    fun deleteAlertRule(ruleId: String) {
        viewModelScope.launch {
            val current = dataStore.getRules()
            dataStore.saveRules(current.filter { it.id != ruleId })
        }
    }

    fun getAlertRules(): Flow<List<AlertRule>> = dataStore.getRulesFlow()
}