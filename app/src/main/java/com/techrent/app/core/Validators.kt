package com.techrent.app.core

object Validators {
    fun email(email: String): String? =
        when {
            email.isBlank() -> "Email requerido"
            !email.contains("@") -> "Formato de email inválido"
            else -> null
        }

    fun password(pw: String): String? =
        when {
            pw.isBlank() -> "Contraseña requerida"
            pw.length < 6 -> "Mínimo 6 caracteres (demo)"
            else -> null
        }

    fun nonEmpty(value: String, field: String): String? =
        if (value.isBlank()) "$field requerido" else null

    fun price(value: String): String? {
        val v = value.toDoubleOrNull() ?: return "Precio inválido"
        return if (v <= 0.0) "Precio debe ser > 0" else null
    }

    fun nonNegativeInt(value: String, field: String): String? {
        val v = value.toIntOrNull() ?: return "$field inválido"
        return if (v < 0) "$field no puede ser negativo" else null
    }
}
