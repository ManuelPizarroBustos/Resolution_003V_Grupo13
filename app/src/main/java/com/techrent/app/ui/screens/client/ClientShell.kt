package com.techrent.app.ui.screens.client

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.techrent.app.App
import com.techrent.app.domain.usecase.CheckoutUseCase
import com.techrent.app.presentation.client.*
import com.techrent.app.ui.navigation.Routes
import com.techrent.app.ui.screens.SimpleVmFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientShell(onGoLogin: () -> Unit) {
    val app = (androidx.compose.ui.platform.LocalContext.current.applicationContext as App)
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()

    val session by app.container.sessionStore.session.collectAsState(
        initial = com.techrent.app.data.datastore.Session(null, null)
    )

    // Evita usar userId = -1 (puede romper FK al insertar en Room si el usuario aún no se carga desde DataStore)
    if (session.userId == null || session.role == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val userId = session.userId!!

    val cartVm: CartViewModel = viewModel(factory = SimpleVmFactory { CartViewModel(app.container.cartRepository) })
    val cartCount by cartVm.count(userId).collectAsState(initial = 0)

    val items = listOf(
        Triple(Routes.Home, Icons.Filled.Home, "Home"),
        Triple(Routes.Cart, Icons.Filled.ShoppingCart, "Carrito"),
        Triple(Routes.History, Icons.Filled.History, "Historial")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TechRent") },
                actions = {
                    AnimatedVisibility(visible = cartCount > 0) {
                        Text("🛒$cartCount", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.width(12.dp))
                    TextButton(onClick = {
                        scope.launch {
                            app.container.authRepository.logout()
                            onGoLogin()
                        }
                    }) { Text("Salir") }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val current = nav.currentBackStackEntryAsState().value?.destination?.route
                items.forEach { (route, icon, label) ->
                    NavigationBarItem(
                        selected = current == route,
                        onClick = { nav.navigate(route) { launchSingleTop = true } },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { pad ->
        NavHost(
            navController = nav,
            startDestination = Routes.Home,
            modifier = Modifier.padding(pad)
        ) {
            composable(Routes.Home) {
                val vm: HomeViewModel = viewModel(factory = SimpleVmFactory { HomeViewModel(app.container.itemRepository) })
                HomeScreen(vm, onOpen = { nav.navigate("${Routes.Detail}/$it") })
            }
            composable(
                "${Routes.Detail}/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.LongType })
            ) { back ->
                val itemId = back.arguments?.getLong("itemId")!!
                val vm: DetailViewModel = viewModel(factory = SimpleVmFactory {
                    DetailViewModel(app.container.itemRepository, app.container.cartRepository)
                })
                ItemDetailScreen(userId = userId, itemId = itemId, vm = vm, onBack = { nav.popBackStack() })
            }
            composable(Routes.Cart) {
                CartScreen(userId = userId, cartVm = cartVm, onCheckout = { nav.navigate(Routes.Checkout) })
            }
            composable(Routes.Checkout) {
                val vm: CheckoutViewModel = viewModel(factory = SimpleVmFactory {
                    CheckoutViewModel(app.container.locationRepository, app.container.orderRepository, CheckoutUseCase())
                })
                CheckoutScreen(
                    userId = userId,
                    cartVm = cartVm,
                    checkoutVm = vm,
                    onDone = { nav.navigate(Routes.History) { popUpTo(Routes.Home) { inclusive = false } } }
                )
            }
            composable(Routes.History) {
                val vm: HistoryViewModel = viewModel(factory = SimpleVmFactory { HistoryViewModel(app.container.orderRepository) })
                HistoryScreen(vm, userId, onOpenOrder = { nav.navigate("${Routes.OrderDetail}/$it") })
            }
            composable(
                "${Routes.OrderDetail}/{orderId}",
                arguments = listOf(navArgument("orderId") { type = NavType.LongType })
            ) { back ->
                val orderId = back.arguments?.getLong("orderId")!!
                val vm: HistoryViewModel = viewModel(factory = SimpleVmFactory { HistoryViewModel(app.container.orderRepository) })
                OrderDetailScreen(vm, orderId, onBack = { nav.popBackStack() })
            }
        }
    }
}
