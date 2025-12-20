package com.techrent.app.ui.screens.admin

import android.Manifest
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.techrent.app.presentation.admin.AdminItemFormViewModel
import com.techrent.app.domain.model.ItemType
import java.io.File

@Composable
fun AdminItemFormScreen(
    vm: AdminItemFormViewModel,
    itemId: Long,
    onDone: () -> Unit
) {
    val s by vm.state.collectAsState()
    val ctx = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(itemId) { vm.load(itemId) }

    var pendingUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        if (ok) vm.onImageUri(pendingUri?.toString())
    }

    val requestCamera = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted && pendingUri != null) takePicture.launch(pendingUri!!)
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text(if (itemId > 0) "Editar ítem" else "Agregar ítem", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = s.name,
            onValueChange = vm::onName,
            label = { Text("Nombre") },
            isError = s.nameError != null,
            supportingText = { if (s.nameError != null) Text(s.nameError!!) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = s.description,
            onValueChange = vm::onDescription,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = s.type == ItemType.SALE, onClick = { vm.onType(ItemType.SALE) }, label = { Text("Venta") })
            FilterChip(selected = s.type == ItemType.RENTAL, onClick = { vm.onType(ItemType.RENTAL) }, label = { Text("Arriendo") })
            FilterChip(selected = s.type == ItemType.SERVICE, onClick = { vm.onType(ItemType.SERVICE) }, label = { Text("Servicio") })
        }

        Spacer(Modifier.height(8.dp))

        if (s.type == ItemType.RENTAL) {
            OutlinedTextField(
                value = s.dailyRate,
                onValueChange = vm::onDailyRate,
                label = { Text("Tarifa diaria") },
                isError = s.dailyRateError != null,
                supportingText = { if (s.dailyRateError != null) Text(s.dailyRateError!!) },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OutlinedTextField(
                value = s.price,
                onValueChange = vm::onPrice,
                label = { Text("Precio") },
                isError = s.priceError != null,
                supportingText = { if (s.priceError != null) Text(s.priceError!!) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = s.stock,
            onValueChange = vm::onStock,
            label = { Text("Stock") },
            isError = s.stockError != null,
            supportingText = { if (s.stockError != null) Text(s.stockError!!) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Switch(checked = s.isAvailable, onCheckedChange = vm::onAvailable)
            Spacer(Modifier.width(8.dp))
            Text("Disponible")
        }

        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            val dir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: ctx.filesDir
            val file = File(dir, "item_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
            pendingUri = uri
            requestCamera.launch(Manifest.permission.CAMERA)
        }) { Text("Tomar foto") }

        if (s.imageUri != null) {
            Spacer(Modifier.height(10.dp))
            Text("Preview:")
            AsyncImage(model = s.imageUri, contentDescription = "preview", modifier = Modifier.fillMaxWidth().height(180.dp))
        }

        Spacer(Modifier.height(16.dp))

        if (s.submitError != null) {
            Text(s.submitError!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = { vm.save(onDone) },
            enabled = s.isValid,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Guardar") }
    }
}
