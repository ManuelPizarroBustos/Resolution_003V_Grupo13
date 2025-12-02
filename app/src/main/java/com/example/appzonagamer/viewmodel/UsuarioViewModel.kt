package com.example.appzonagamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appzonagamer.model.UsuarioErrores
import com.example.appzonagamer.model.UsuarioUiState
import com.example.appzonagamer.di.DataSourceModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.getValue

class UsuarioViewModel : ViewModel() {

    // Acceso perezoso al repositorio (así los tests no revientan)
    private val repository by lazy { DataSourceModule.userRepository }

    private val _estado = MutableStateFlow(value = UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado

    fun registrarUsuario() {
        val estadoActual = _estado.value
        viewModelScope.launch {
            repository.guardarUsuarioLocal(estadoActual)
        }
    }





    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null),) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null),) }
    }

    fun onContrasenaChange(valor: String) {
        _estado.update { it.copy(contrasena = valor, errores = it.errores.copy(contrasena = null),) }
    }

    fun onTelefonoChange(valor: String) {
        _estado.update { it.copy(telefono = valor, errores = it.errores.copy(telefono = null),) }

    }

    fun onFavoritosChange(valor: String) {
        _estado.update { it.copy(favoritos = valor, errores = it.errores.copy(favoritos = null),) }

    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor,) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = when {
                estadoActual.nombre.isBlank() ->
                    "Campo obligatorio"

                estadoActual.nombre.length > 100 ->
                    "Máximo 100 caracteres"

                !estadoActual.nombre.all { it.isLetter() || it == ' ' } ->
                    "Solo se permiten letras y espacios"

                else -> null
            },
            correo = when {
                estadoActual.correo.isBlank() ->
                    "Campo obligatorio"

                estadoActual.correo.length > 60 ->
                    "Máximo 60 caracteres"

                !estadoActual.correo.endsWith("@duoc.cl") ->
                    "Debe ser un correo @duoc.cl"

                else -> null
            },
            contrasena = when {
                estadoActual.contrasena.isBlank() ->
                    "Campo obligatorio"

                estadoActual.contrasena.length < 10 ->
                    "Debe tener al menos 10 caracteres"

                !estadoActual.contrasena.any { it.isUpperCase() } ->
                    "Debe incluir al menos una letra mayúscula"

                !estadoActual.contrasena.any { it.isLowerCase() } ->
                    "Debe incluir al menos una letra minúscula"

                !estadoActual.contrasena.any { it.isDigit() } ->
                    "Debe incluir al menos un número"

                !estadoActual.contrasena.any { !it.isLetterOrDigit() } ->
                    "Debe incluir al menos un carácter especial (ej: @#$%)"

                else -> null
            },
            telefono = if (estadoActual.telefono.isBlank()) "Campo obligatorio" else null,
            favoritos = if (estadoActual.favoritos.isBlank()) "Selecciona un genero almenos" else null,
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.contrasena,
            errores.telefono,
            errores.favoritos
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores,) }

        return !hayErrores

    }
    fun cargarCiudadDesdeApi() {
        viewModelScope.launch {
            try {
                val ciudad = repository.obtenerCiudadRandom()

                _estado.update { estadoActual ->
                    estadoActual.copy(
                        errores = estadoActual.errores.copy(
                            city = ciudad.ifBlank { "No se pudo obtener la ciudad" }
                        )
                    )
                }
            } catch (e: Exception) {
                _estado.update { estadoActual ->
                    estadoActual.copy(
                        errores = estadoActual.errores.copy(
                            city = "Error al consultar la API"
                        )
                    )
                }
            }
        }
    }
}
