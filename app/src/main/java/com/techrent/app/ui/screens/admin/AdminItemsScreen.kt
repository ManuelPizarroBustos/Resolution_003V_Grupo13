package com.techrent.app.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techrent.app.core.UiState
import com.techrent.app.presentation.admin.AdminItemsViewModel

@Composable
fun AdminItemsScreen(
    vm: AdminItemsViewModel,
    onAdd: () -> Unit,
    onEdit: (Long) -> Unit,
    onOrders: () -> Unit
) {
    val state by vm.items.collectAsState()

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onAdd) { Text("Agregar ítem") }
            OutlinedButton(onClick = onOrders) { Text("Ver pedidos") }
        }
        Spacer(Modifier.height(12.dp))

        when (val s = state) {
            UiState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
            is UiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
            is UiState.Success -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(s.data) { it ->
                        ElevatedCard(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp)) {
                                Text(it.name, style = MaterialTheme.typography.titleMedium)
                                Text("Tipo: ${it.type} • Stock: ${it.stock} • Disponible: ${it.isAvailable}")
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(onClick = { onEdit(it.id) }) { Text("Editar") }
                                    TextButton(onClick = { vm.delete(it) }) { Text("Eliminar") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
