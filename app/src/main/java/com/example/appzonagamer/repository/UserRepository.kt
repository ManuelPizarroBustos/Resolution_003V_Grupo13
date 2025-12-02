package com.example.appzonagamer.repository


import com.example.appzonagamer.datasource.UsuarioDao
import com.example.appzonagamer.datasource.RestDataSource
import com.example.appzonagamer.datasource.UsuarioEntity
import com.example.appzonagamer.model.UsuarioUiState

class UserRepository(
    private val usuarioDao: UsuarioDao,
    private val restDataSource: RestDataSource
) {

    // ------- ROOM (almacenamiento local) -------

    suspend fun guardarUsuarioLocal(estado: UsuarioUiState) {
        val entity = UsuarioEntity(
            nombre = estado.nombre,
            correo = estado.correo,
            contrasena = estado.contrasena,
            telefono = estado.telefono,
            favoritos = estado.favoritos,
            aceptaTerminos = estado.aceptaTerminos
        )
        usuarioDao.insertar(entity)
    }

    suspend fun obtenerUsuariosLocales(): List<UsuarioEntity> =
        usuarioDao.obtenerTodos()

    // ------- API EXTERNA (RandomUser) -------

    suspend fun obtenerCiudadRandom(): String {
        val respuesta = restDataSource.getUsuarioLocation()
        val location = respuesta.results.firstOrNull()?.location
        return location?.city ?: ""
    }
}
