package com.techrent.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.techrent.app.App
import com.techrent.app.presentation.auth.SplashViewModel
import com.techrent.app.presentation.auth.StartDestination

@Composable
fun SplashScreen(
    onGoLogin: () -> Unit,
    onGoClient: () -> Unit,
    onGoAdmin: () -> Unit
) {
    val app = (androidx.compose.ui.platform.LocalContext.current.applicationContext as App)
    val vm: SplashViewModel = viewModel(factory = SimpleVmFactory { SplashViewModel(app.container.authRepository) })

    val dest by vm.dest.collectAsState()

    LaunchedEffect(Unit) { vm.decide() }
    LaunchedEffect(dest) {
        when (dest) {
            StartDestination.Login -> onGoLogin()
            StartDestination.Client -> onGoClient()
            StartDestination.Admin -> onGoAdmin()
        }
    }

    Surface {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TechRent", style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(12.dp))
                CircularProgressIndicator()
            }
        }
    }
}
