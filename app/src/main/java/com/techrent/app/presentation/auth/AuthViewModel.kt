package com.techrent.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techrent.app.core.UiState
import com.techrent.app.core.Validators
import com.techrent.app.data.repository.AuthRepository
import com.techrent.app.domain.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginForm(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null
) {
    val isValid: Boolean get() = emailError == null && passwordError == null && email.isNotBlank() && password.isNotBlank()
}

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow<UiState<Role>>(UiState.Success(Role.CLIENT))
    val state: StateFlow<UiState<Role>> = _state

    private val _form = MutableStateFlow(LoginForm())
    val form: StateFlow<LoginForm> = _form

    fun onEmail(v: String) {
        _form.value = _form.value.copy(email = v, emailError = Validators.email(v))
    }

    fun onPassword(v: String) {
        _form.value = _form.value.copy(password = v, passwordError = Validators.password(v))
    }

    fun login() {
        val f = _form.value.copy(
            emailError = Validators.email(_form.value.email),
            passwordError = Validators.password(_form.value.password)
        )
        _form.value = f
        if (!f.isValid) return

        viewModelScope.launch {
            _state.value = UiState.Loading
            val res = repo.login(f.email.trim(), f.password)
            _state.value = res.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Error login") }
            )
        }
    }

    fun logout() {
        viewModelScope.launch { repo.logout() }
    }
}
