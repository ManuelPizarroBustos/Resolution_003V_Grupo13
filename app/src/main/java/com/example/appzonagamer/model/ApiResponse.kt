package com.example.appzonagamer.model

data class ApiResponse(
    val results: List<Results> = emptyList(),
)

data class Results(
    val name: UsuarioErrores?,
    val location: UsuarioLocation?,
    val picture: UsuarioImagen?,
)