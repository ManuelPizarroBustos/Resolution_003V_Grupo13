package com.example.appzonagamer.model


data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val telefono: String = "",
    val favoritos: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
)

