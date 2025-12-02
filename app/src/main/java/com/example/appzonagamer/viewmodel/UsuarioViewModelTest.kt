package com.example.appzonagamer.viewmodel


import kotlin.test.assertFalse
import kotlin.test.assertTrue




class UsuarioViewModelTest {

    private lateinit var viewModel: UsuarioViewModel

    @Before
    fun setup() {
        // No hace falta repo, porque no usaremos registrarUsuario() ni cargarCiudadDesdeApi()
        viewModel = UsuarioViewModel()
    }

    @Test
    fun `cuando nombre esta vacio el formulario no es valido`() {
        viewModel.onNombreChange("")
        viewModel.onCorreoChange("alguien@duoc.cl")
        viewModel.onContrasenaChange("ClaveSegura1!")
        viewModel.onTelefonoChange("123456789")
        viewModel.onFavoritosChange("League of Legends")
        viewModel.onAceptarTerminosChange(true)

        val esValido = viewModel.validarFormulario()

        assertFalse(esValido)
    }

    @Test
    fun `cuando todos los campos son correctos el formulario es valido`() {
        viewModel.onNombreChange("Juan Pérez")
        viewModel.onCorreoChange("juan.perez@duoc.cl")
        viewModel.onContrasenaChange("ClaveSegura1!")
        viewModel.onTelefonoChange("123456789")
        viewModel.onFavoritosChange("Valorant")
        viewModel.onAceptarTerminosChange(true)

        val esValido = viewModel.validarFormulario()

        assertTrue(esValido)
    }
}

annotation class Test

annotation class Before
