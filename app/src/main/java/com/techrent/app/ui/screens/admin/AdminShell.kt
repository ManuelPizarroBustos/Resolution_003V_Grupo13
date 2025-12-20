package com.techrent.app.ui.screens.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.techrent.app.App
import com.techrent.app.presentation.admin.AdminItemFormViewModel
import com.techrent.app.presentation.admin.AdminItemsViewModel
import com.techrent.app.presentation.admin.AdminOrdersViewModel
import com.techrent.app.ui.navigation.Routes
import com.techrent.app.ui.screens.SimpleVmFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminShell(onGoLogin: () -> Unit) {
    val app = (androidx.compose.ui.platform.LocalContext.current.applicationContext as App)
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel") },
                actions = {
                    TextButton(onClick = {
                        scope.launch {
                            app.container.authRepository.logout()
                            onGoLogin()
                        }
                    }) { Text("Salir") }
                }
            )
        }
    ) { pad ->
        NavHost(
            navController = nav,
            startDestination = Routes.AdminItems,
            modifier = Modifier.padding(pad)
        ) {
            composable(Routes.AdminItems) {
                val vm: AdminItemsViewModel = viewModel(factory = SimpleVmFactory { AdminItemsViewModel(app.container.itemRepository) })
                AdminItemsScreen(
                    vm = vm,
                    onAdd = { nav.navigate(Routes.AdminItemForm) },
                    onEdit = { nav.navigate("${Routes.AdminItemForm}?itemId=$it") },
                    onOrders = { nav.navigate(Routes.AdminOrders) }
                )
            }
            composable(
                "${Routes.AdminItemForm}?itemId={itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.LongType; defaultValue = -1L })
            ) { back ->
                val itemId = back.arguments?.getLong("itemId") ?: -1L
                val vm: AdminItemFormViewModel = viewModel(factory = SimpleVmFactory { AdminItemFormViewModel(app.container.itemRepository) })
                AdminItemFormScreen(vm = vm, itemId = itemId, onDone = { nav.popBackStack() })
            }
            composable(Routes.AdminOrders) {
                val vm: AdminOrdersViewModel = viewModel(factory = SimpleVmFactory { AdminOrdersViewModel(app.container.orderRepository) })
                AdminOrdersScreen(vm = vm, onOpen = { nav.navigate("${Routes.AdminOrderDetail}/$it") }, onBack = { nav.popBackStack() })
            }
            composable(
                "${Routes.AdminOrderDetail}/{orderId}",
                arguments = listOf(navArgument("orderId") { type = NavType.LongType })
            ) { back ->
                val orderId = back.arguments?.getLong("orderId")!!
                val vm: AdminOrdersViewModel = viewModel(factory = SimpleVmFactory { AdminOrdersViewModel(app.container.orderRepository) })
                AdminOrderDetailScreen(vm = vm, orderId = orderId, onBack = { nav.popBackStack() })
            }
        }
    }
}
