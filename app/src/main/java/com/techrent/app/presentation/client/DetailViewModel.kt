package com.techrent.app.presentation.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techrent.app.core.DateTime
import com.techrent.app.core.UiState
import com.techrent.app.data.local.entity.ItemEntity
import com.techrent.app.data.repository.CartRepository
import com.techrent.app.data.repository.ItemRepository
import com.techrent.app.domain.model.ItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RentalForm(
    val start: String = "",
    val end: String = "",
    val startError: String? = null,
    val endError: String? = null
) {
    val valid: Boolean get() = startError == null && endError == null
}

class DetailViewModel(
    private val itemRepo: ItemRepository,
    private val cartRepo: CartRepository
) : ViewModel() {

    private val _itemState = MutableStateFlow<UiState<ItemEntity>>(UiState.Loading)
    val itemState: StateFlow<UiState<ItemEntity>> = _itemState

    private val _rental = MutableStateFlow(RentalForm())
    val rental: StateFlow<RentalForm> = _rental

    private val _snack = MutableStateFlow<String?>(null)
    val snack: StateFlow<String?> = _snack

    fun load(itemId: Long) {
        viewModelScope.launch {
            _itemState.value = UiState.Loading
            val item = itemRepo.findById(itemId)
            _itemState.value = if (item != null) UiState.Success(item) else UiState.Error("Ítem no encontrado")
        }
    }

    fun onStart(v: String) {
        _rental.value = _rental.value.copy(start = v, startError = validateDate(v, "Inicio"))
    }

    fun onEnd(v: String) {
        _rental.value = _rental.value.copy(end = v, endError = validateDate(v, "Fin"))
    }

    private fun validateDate(v: String, label: String): String? =
        if (DateTime.parseDateOrNull(v) == null) "$label inválido (yyyy-MM-dd)" else null

    fun addToCart(userId: Long, item: ItemEntity) {
        viewModelScope.launch {
            if (userId <= 0) {
                _snack.value = "Sesión no lista. Espera un segundo y vuelve a intentar."
                return@launch
            }
            val (start, end) = if (item.type == ItemType.RENTAL) {
                val f = _rental.value.copy(
                    startError = validateDate(_rental.value.start, "Inicio"),
                    endError = validateDate(_rental.value.end, "Fin")
                )
                _rental.value = f
                if (!f.valid) return@launch

                val s = DateTime.parseDateOrNull(f.start)!!
                val e = DateTime.parseDateOrNull(f.end)!!
                if (!e.isAfter(s)) {
                    _rental.value = f.copy(endError = "Fin debe ser posterior a inicio")
                    return@launch
                }
                (f.start to f.end)
            } else (null to null)

            val res = cartRepo.addOrIncrement(userId, item.id, 1, start, end)
            _snack.value = res.fold(
                onSuccess = { "Agregado al carrito" },
                onFailure = { it.message ?: "Error al agregar" }
            )
        }
    }

    fun consumeSnack() { _snack.value = null }
}
