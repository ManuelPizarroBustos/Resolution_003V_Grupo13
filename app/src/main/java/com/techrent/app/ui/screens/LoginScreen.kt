package com.techrent.app.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.techrent.app.App
import com.techrent.app.core.UiState
import com.techrent.app.domain.model.Role
import com.techrent.app.presentation.auth.AuthViewModel

@Composable
fun LoginScreen(
    onLoggedAsClient: () -> Unit,
    onLoggedAsAdmin: () -> Unit
) {
    val app = (androidx.compose.ui.platform.LocalContext.current.applicationContext as App)
    val vm: AuthViewModel = viewModel(factory = SimpleVmFactory { AuthViewModel(app.container.authRepository) })

    val form by vm.form.collectAsState()
    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state is UiState.Success) {
            when ((state as UiState.Success<Role>).data) {
                Role.CLIENT -> onLoggedAsClient()
                Role.ADMIN -> onLoggedAsAdmin()
            }
        }
    }

    Surface {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = form.email,
                onValueChange = vm::onEmail,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                isError = form.emailError != null,
                supportingText = { if (form.emailError != null) Text(form.emailError!!) }
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = form.password,
                onValueChange = vm::onPassword,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = form.passwordError != null,
                supportingText = { if (form.passwordError != null) Text(form.passwordError!!) }
            )
            Spacer(Modifier.height(16.dp))

            Crossfade(targetState = state, label = "loginState") { s ->
                when (s) {
                    UiState.Loading -> CircularProgressIndicator()
                    is UiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
                    else -> Spacer(Modifier.height(0.dp))
                }
            }

            Spacer(Modifier.height(10.dp))
            Button(
                onClick = vm::login,
                enabled = form.isValid && state !is UiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Entrar") }

            Spacer(Modifier.height(10.dp))
            Text(
                "Credenciales seed:\nadmin@demo.cl / Admin123!\ncliente@demo.cl / Cliente123!",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
