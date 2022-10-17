package com.example.jetpackdatastoresample.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class Prefs(private val dataStore: DataStore<Preferences>) {

    val userPreferencesWordFlow: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val word = preferences[WORD] ?: ""

            word
        }

    val userPreferencesCounterFlow: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit( emptyPreferences())
            }else {
                throw exception
            }
        }.map { preferences ->
            val counter = preferences[COUNTER] ?: 0

            counter
        }

    companion object{
        // Name of the data store
        const val PREFS_NAME = "settings"

        // Name of the keys used to store key-value pairs on data store
        private val WORD = stringPreferencesKey("key_word")
        private val COUNTER = intPreferencesKey("key_counter")

    }

    suspend fun saveWord(word: String){
        dataStore.edit { preferences ->
            preferences[WORD] = word
        }
    }

    suspend fun incrementCounter(){
        dataStore.edit { preferences ->
            val currentCounterValue = preferences[COUNTER] ?: 0
            preferences[COUNTER] = currentCounterValue + 1
        }
    }

    suspend fun decrementCounter(){
        dataStore.edit { preferences ->
            val currentCounterValue = preferences[COUNTER] ?: 0
            if (currentCounterValue > 0)
                preferences[COUNTER] = currentCounterValue - 1
        }
    }

}