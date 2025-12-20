package com.techrent.app.data.repository

import com.techrent.app.core.Crypto
import com.techrent.app.data.datastore.SessionStore
import com.techrent.app.data.local.dao.UserDao
import com.techrent.app.domain.model.Role
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val userDao: UserDao,
    private val sessionStore: SessionStore
) {
    val session: Flow<com.techrent.app.data.datastore.Session> = sessionStore.session

    suspend fun login(email: String, password: String): Result<Role> {
        val user = userDao.findByEmail(email) ?: return Result.failure(Exception("Usuario no existe"))
        val hash = Crypto.sha256(password)
        if (user.passwordHash != hash) return Result.failure(Exception("Contraseña incorrecta"))

        sessionStore.saveSession(user.id, user.role)
        return Result.success(user.role)
    }

    suspend fun logout() = sessionStore.clearSession()
}
