package com.techrent.app.ui.screens.client

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techrent.app.core.UiState
import com.techrent.app.presentation.client.HistoryViewModel

@Composable
fun HistoryScreen(vm: HistoryViewModel, userId: Long, onOpenOrder: (Long) -> Unit) {
    val state by remember(userId) { vm.orders(userId) }.collectAsState()

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Historial", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(10.dp))

        when (val s = state) {
            UiState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
            is UiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
            is UiState.Success -> {
                val orders = s.data
                if (orders.isEmpty()) {
                    Text("Aún no tienes órdenes/solicitudes.")
                    return@Column
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(orders) { ow ->
                        ElevatedCard(
                            Modifier.fillMaxWidth().clickable { onOpenOrder(ow.order.id) }
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text("Orden #${ow.order.id} • ${ow.order.orderType} • ${ow.order.status}")
                                Text("Total: ${ow.order.total}")
                                if (ow.order.lat != null) {
                                    Text(
                                        "Ubicación: ${ow.order.lat}, ${ow.order.lng}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                Text("Líneas: ${ow.lines.size}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
