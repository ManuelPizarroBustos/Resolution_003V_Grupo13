package com.techrent.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techrent.app.data.repository.AuthRepository
import com.techrent.app.domain.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class StartDestination {
    data object Login : StartDestination()
    data object Client : StartDestination()
    data object Admin : StartDestination()
}

class SplashViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _dest = MutableStateFlow<StartDestination>(StartDestination.Login)
    val dest: StateFlow<StartDestination> = _dest

    fun decide() {
        viewModelScope.launch {
            val s = authRepository.session.first()
            _dest.value = when (s.role) {
                Role.ADMIN -> StartDestination.Admin
                Role.CLIENT -> StartDestination.Client
                else -> StartDestination.Login
            }
        }
    }
}
