package com.techrent.app.ui.screens.client

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techrent.app.core.UiState
import com.techrent.app.presentation.client.CartViewModel
import com.techrent.app.presentation.client.CheckoutViewModel

@Composable
fun CheckoutScreen(
    userId: Long,
    cartVm: CartViewModel,
    checkoutVm: CheckoutViewModel,
    onDone: () -> Unit
) {
    val cartState by cartVm.cart(userId).collectAsState()
    val ui by checkoutVm.ui.collectAsState()
    val state by checkoutVm.state.collectAsState()

    val requestLocation = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) checkoutVm.fetchLocation()
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Checkout", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(10.dp))

        val cart = (cartState as? UiState.Success)?.data ?: emptyList()
        Text("Items: ${cart.size}")

        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Switch(
                checked = ui.useLocation,
                onCheckedChange = {
                    checkoutVm.toggleUseLocation(it)
                    if (it) requestLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            )
            Spacer(Modifier.width(10.dp))
            Text("Usar mi ubicación (GPS)")
        }
        if (ui.useLocation) {
            Text("lat: ${ui.lat ?: "-"}  lng: ${ui.lng ?: "-"}", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { checkoutVm.confirm(userId, cart) },
            enabled = cart.isNotEmpty() && state !is UiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Confirmar") }

        Spacer(Modifier.height(12.dp))
        Crossfade(targetState = state, label = "checkoutState") { s ->
            when (s) {
                UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> if (s.data > 0) {
                    Text("Orden creada: #${s.data}")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onDone) { Text("Ver historial") }
                } else Unit
            }
        }
    }
}
