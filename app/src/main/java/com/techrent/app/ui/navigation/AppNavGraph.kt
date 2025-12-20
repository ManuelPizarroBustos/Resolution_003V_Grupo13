package com.techrent.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techrent.app.ui.screens.LoginScreen
import com.techrent.app.ui.screens.SplashScreen
import com.techrent.app.ui.screens.admin.AdminShell
import com.techrent.app.ui.screens.client.ClientShell

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.Splash) {

        composable(Routes.Splash) {
            SplashScreen(
                onGoLogin = { nav.navigate(Routes.Login) { popUpTo(Routes.Splash) { inclusive = true } } },
                onGoClient = { nav.navigate(Routes.ClientShell) { popUpTo(Routes.Splash) { inclusive = true } } },
                onGoAdmin = { nav.navigate(Routes.AdminShell) { popUpTo(Routes.Splash) { inclusive = true } } }
            )
        }

        composable(Routes.Login) {
            LoginScreen(
                onLoggedAsClient = { nav.navigate(Routes.ClientShell) { popUpTo(Routes.Login) { inclusive = true } } },
                onLoggedAsAdmin = { nav.navigate(Routes.AdminShell) { popUpTo(Routes.Login) { inclusive = true } } }
            )
        }

        composable(Routes.ClientShell) {
            ClientShell(onGoLogin = { nav.navigate(Routes.Login) { popUpTo(0) } })
        }

        composable(Routes.AdminShell) {
            AdminShell(onGoLogin = { nav.navigate(Routes.Login) { popUpTo(0) } })
        }
    }
}
