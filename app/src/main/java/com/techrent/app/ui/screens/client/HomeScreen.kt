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
import com.techrent.app.domain.model.ItemType
import com.techrent.app.presentation.client.HomeViewModel

@Composable
fun HomeScreen(vm: HomeViewModel, onOpen: (Long) -> Unit) {
    val state by vm.uiState.collectAsState()
    val filter by vm.filter.collectAsState()

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        OutlinedTextField(
            value = filter.query,
            onValueChange = vm::onQuery,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar") }
        )
        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = filter.type == null, onClick = { vm.onType(null) }, label = { Text("Todos") })
            FilterChip(selected = filter.type == ItemType.SALE, onClick = { vm.onType(ItemType.SALE) }, label = { Text("Venta") })
            FilterChip(selected = filter.type == ItemType.RENTAL, onClick = { vm.onType(ItemType.RENTAL) }, label = { Text("Arriendo") })
            FilterChip(selected = filter.type == ItemType.SERVICE, onClick = { vm.onType(ItemType.SERVICE) }, label = { Text("Servicio") })
        }

        Spacer(Modifier.height(12.dp))

        when (val s = state) {
            UiState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
            is UiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
            is UiState.Success -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(s.data) { item ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth().clickable { onOpen(item.id) }
                        ) {
                            Column(Modifier.padding(14.dp)) {
                                Text(item.name, style = MaterialTheme.typography.titleMedium)
                                Text(item.description, style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.height(6.dp))
                                Text("Tipo: ${item.type} • Stock: ${item.stock} • Disponible: ${item.isAvailable}")
                            }
                        }
                    }
                }
            }
        }
    }
}
