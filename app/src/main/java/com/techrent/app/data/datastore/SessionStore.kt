package com.techrent.app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.techrent.app.domain.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.ds by preferencesDataStore("session_store")

data class Session(val userId: Long?, val role: Role?)

class SessionStore(private val context: Context) {
    private val KEY_USER_ID = longPreferencesKey("user_id")
    private val KEY_ROLE = stringPreferencesKey("role")
    private val KEY_SEEDED = booleanPreferencesKey("seeded")

    val session: Flow<Session> = context.ds.data.map { prefs ->
        val id = prefs[KEY_USER_ID]
        val role = prefs[KEY_ROLE]?.let { runCatching { Role.valueOf(it) }.getOrNull() }
        Session(id, role)
    }

    val seeded: Flow<Boolean> = context.ds.data.map { it[KEY_SEEDED] ?: false }

    suspend fun saveSession(userId: Long, role: Role) {
        context.ds.edit {
            it[KEY_USER_ID] = userId
            it[KEY_ROLE] = role.name
        }
    }

    suspend fun clearSession() {
        context.ds.edit {
            it.remove(KEY_USER_ID)
            it.remove(KEY_ROLE)
        }
    }

    suspend fun setSeeded() {
        context.ds.edit { it[KEY_SEEDED] = true }
    }
}
