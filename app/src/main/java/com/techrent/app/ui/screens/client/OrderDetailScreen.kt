package com.techrent.app.ui.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techrent.app.core.UiState
import com.techrent.app.presentation.client.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(vm: HistoryViewModel, orderId: Long, onBack: () -> Unit) {
    val state by remember(orderId) { vm.order(orderId) }.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orden #$orderId") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } }
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(12.dp)) {
            when (val s = state) {
                UiState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
                is UiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> {
                    val data = s.data
                    Text("Tipo: ${data.order.orderType} • Estado: ${data.order.status}")
                    Text("Total: ${data.order.total}")
                    if (data.order.lat != null) {
                        Text("Ubicación: ${data.order.lat}, ${data.order.lng}", style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Detalle", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(data.lines) { line ->
                            ElevatedCard(Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(12.dp)) {
                                    Text("ItemId: ${line.itemId} • Qty: ${line.qty}")
                                    Text("Unit: ${line.unitPrice} • Subtotal: ${line.subtotal}")
                                    if (line.rentalDays != null) Text("Días arriendo: ${line.rentalDays}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
