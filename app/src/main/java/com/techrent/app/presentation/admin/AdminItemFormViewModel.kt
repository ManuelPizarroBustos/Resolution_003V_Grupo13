package com.techrent.app.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techrent.app.core.DateTime
import com.techrent.app.core.Validators
import com.techrent.app.data.local.entity.ItemEntity
import com.techrent.app.data.repository.ItemRepository
import com.techrent.app.domain.model.ItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminItemFormState(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val type: ItemType = ItemType.SALE,
    val price: String = "",
    val dailyRate: String = "",
    val stock: String = "0",
    val isAvailable: Boolean = true,
    val imageUri: String? = null,

    val nameError: String? = null,
    val priceError: String? = null,
    val dailyRateError: String? = null,
    val stockError: String? = null
    ,
    val submitError: String? = null
) {
    val isValid: Boolean
        get() = listOf(nameError, priceError, dailyRateError, stockError).all { it == null } &&
            name.isNotBlank() && stock.isNotBlank() &&
            (type == ItemType.RENTAL || price.isNotBlank()) &&
            (type != ItemType.RENTAL || dailyRate.isNotBlank())
}

class AdminItemFormViewModel(private val repo: ItemRepository) : ViewModel() {
    private val _state = MutableStateFlow(AdminItemFormState())
    val state: StateFlow<AdminItemFormState> = _state

    private var createdAt: Long = 0

    fun load(itemId: Long) {
        if (itemId <= 0) return
        viewModelScope.launch {
            val it = repo.findById(itemId) ?: return@launch
            createdAt = it.createdAt
            _state.value = AdminItemFormState(
                id = it.id,
                name = it.name,
                description = it.description,
                type = it.type,
                price = it.price.toString(),
                dailyRate = it.dailyRate?.toString() ?: "",
                stock = it.stock.toString(),
                isAvailable = it.isAvailable,
                imageUri = it.imageUri
            ).validateAll()
        }
    }

    fun onName(v: String) { _state.value = _state.value.copy(name = v).validateAll() }
    fun onDescription(v: String) { _state.value = _state.value.copy(description = v) }
    fun onType(v: ItemType) { _state.value = _state.value.copy(type = v).validateAll() }
    fun onPrice(v: String) { _state.value = _state.value.copy(price = v).validateAll() }
    fun onDailyRate(v: String) { _state.value = _state.value.copy(dailyRate = v).validateAll() }
    fun onStock(v: String) { _state.value = _state.value.copy(stock = v).validateAll() }
    fun onAvailable(v: Boolean) { _state.value = _state.value.copy(isAvailable = v) }
    fun onImageUri(v: String?) { _state.value = _state.value.copy(imageUri = v) }

    fun save(onDone: () -> Unit) {
        val s = _state.value.validateAll()
        _state.value = s.copy(submitError = null)
        if (!s.isValid) return

        viewModelScope.launch {
            runCatching {
                val now = DateTime.nowMillis()
                val entity = ItemEntity(
                    id = s.id,
                    name = s.name.trim(),
                    description = s.description.trim(),
                    type = s.type,
                    price = if (s.type == ItemType.RENTAL) 0.0 else s.price.toDouble(),
                    dailyRate = if (s.type == ItemType.RENTAL) s.dailyRate.toDouble() else null,
                    stock = s.stock.toInt(),
                    isAvailable = s.isAvailable,
                    imageUri = s.imageUri,
                    createdAt = if (createdAt != 0L) createdAt else now
                )
                repo.upsert(entity)
            }.onSuccess {
                onDone()
            }.onFailure { e ->
                _state.value = _state.value.copy(submitError = e.message ?: "No se pudo guardar")
            }
        }
    }

    private fun AdminItemFormState.validateAll(): AdminItemFormState {
        val nameErr = Validators.nonEmpty(name, "Nombre")
        val stockErr = Validators.nonNegativeInt(stock, "Stock")
        val priceErr = if (type == ItemType.RENTAL) null else Validators.price(price)
        val dailyErr = if (type == ItemType.RENTAL) Validators.price(dailyRate) else null
        return copy(nameError = nameErr, stockError = stockErr, priceError = priceErr, dailyRateError = dailyErr)
    }
}
