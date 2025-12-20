package com.techrent.app.ui.screens.client

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techrent.app.core.UiState
import com.techrent.app.domain.model.ItemType
import com.techrent.app.presentation.client.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    userId: Long,
    itemId: Long,
    vm: DetailViewModel,
    onBack: () -> Unit
) {
    val state by vm.itemState.collectAsStateWithLifecycle()
    val rental by vm.rental.collectAsStateWithLifecycle()
    val snack by vm.snack.collectAsStateWithLifecycle()
    val snackbarHost = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { vm.load(itemId) }
    LaunchedEffect(snack) {
        if (snack != null) {
            snackbarHost.showSnackbar(snack!!)
            vm.consumeSnack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHost) }
    ) { pad ->
        Column(Modifier.padding(pad).padding(14.dp).animateContentSize()) {
            when (val s = state) {
                UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> {
                    val item = s.data
                    Text(item.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Text(item.description)
                    Spacer(Modifier.height(8.dp))

                    if (item.type == ItemType.RENTAL) {
                        Text("Tarifa/día: ${item.dailyRate}")
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = rental.start,
                            onValueChange = vm::onStart,
                            label = { Text("Inicio arriendo (yyyy-MM-dd)") },
                            isError = rental.startError != null,
                            supportingText = { if (rental.startError != null) Text(rental.startError!!) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = rental.end,
                            onValueChange = vm::onEnd,
                            label = { Text("Fin arriendo (yyyy-MM-dd)") },
                            isError = rental.endError != null,
                            supportingText = { if (rental.endError != null) Text(rental.endError!!) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Precio: ${item.price}")
                    }

                    Spacer(Modifier.height(14.dp))
                    Button(
                        onClick = { vm.addToCart(userId, item) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Agregar al carrito") }
                }
            }
        }
    }
}
