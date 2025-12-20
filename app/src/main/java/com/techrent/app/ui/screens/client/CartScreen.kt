package com.techrent.app.ui.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techrent.app.core.UiState
import com.techrent.app.presentation.client.CartViewModel

@Composable
fun CartScreen(userId: Long, cartVm: CartViewModel, onCheckout: () -> Unit) {
    val state by cartVm.cart(userId).collectAsState()

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Carrito", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(10.dp))

        when (val s = state) {
            UiState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
            is UiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
            is UiState.Success -> {
                if (s.data.isEmpty()) {
                    Text("Carrito vacío")
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(s.data) { row ->
                            ElevatedCard(Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(row.item.name, style = MaterialTheme.typography.titleMedium)
                                    Text("Qty: ${row.cart.qty}")
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedButton(onClick = { cartVm.dec(userId, row.item.id, row.cart.qty) }) { Text("–") }
                                        OutlinedButton(onClick = { cartVm.inc(userId, row.item.id, row.cart.qty) }) { Text("+") }
                                        TextButton(onClick = { cartVm.remove(row.cart.id) }) { Text("Eliminar") }
                                    }
                                }
                            }
                        }
                    }
                    Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth()) { Text("Ir a Checkout") }
                }
            }
        }
    }
}
