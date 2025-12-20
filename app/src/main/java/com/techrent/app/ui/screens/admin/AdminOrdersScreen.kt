package com.techrent.app.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techrent.app.core.UiState
import com.techrent.app.presentation.admin.AdminOrdersViewModel

@Composable
fun AdminOrdersScreen(vm: AdminOrdersViewModel, onOpen: (Long) -> Unit, onBack: () -> Unit) {
    val state by vm.orders.collectAsState()

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        TextButton(onClick = onBack) { Text("Atrás") }
        Text("Pedidos / Solicitudes", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(10.dp))

        when (val s = state) {
            UiState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
            is UiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
            is UiState.Success -> {
                val orders = s.data
                if (orders.isEmpty()) {
                    Text("Aún no hay pedidos.")
                    return@Column
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(orders) { ow ->
                        ElevatedCard(Modifier.fillMaxWidth().clickable { onOpen(ow.order.id) }) {
                            Column(Modifier.padding(12.dp)) {
                                Text("Orden #${ow.order.id} • ${ow.order.orderType}")
                                Text("Estado: ${ow.order.status} • Total: ${ow.order.total}")
                                Text("Líneas: ${ow.lines.size}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
