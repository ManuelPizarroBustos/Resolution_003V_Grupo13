package com.example.appzonagamer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appzonagamer.viewmodel.UsuarioViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
)
{
    val estado by viewModel.estado.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text(text = "Nombre") },
            isError = estado.errores.nombre != null,
            supportingText = {
                estado.errores.nombre?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            label = { Text(text = "Correo electrónico") },
            isError = estado.errores.correo != null,
            supportingText = {
                estado.errores.correo?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = estado.contrasena,
            onValueChange = viewModel::onContrasenaChange,
            label = { Text(text = "Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = estado.errores.contrasena != null,
            supportingText = {
                estado.errores.contrasena?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.telefono,
            onValueChange = viewModel::onTelefonoChange,
            label = { Text(text = "Telefono") },
            isError = estado.errores.telefono != null,
            supportingText = {
                estado.errores.telefono?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )


        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = estado.aceptaTerminos,
                onCheckedChange = viewModel::onAceptarTerminosChange
            )
            Spacer(Modifier.width(width = 8.dp))
            Text(text = "Acepto los términos y condiciones")
        }


        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    viewModel.registrarUsuario()  // 🔥 Guarda en Room
                    navController.navigate(route = "resumen")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar")
        }
        Button(
            onClick = { viewModel.cargarCiudadDesdeApi() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sugerir ciudad desde API externa")
        }


    }

}