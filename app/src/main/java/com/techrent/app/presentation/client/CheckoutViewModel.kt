package com.techrent.app.presentation.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techrent.app.core.UiState
import com.techrent.app.data.location.LocationRepository
import com.techrent.app.data.local.model.CartItemWithItem
import com.techrent.app.data.repository.OrderRepository
import com.techrent.app.domain.usecase.CheckoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CheckoutUi(
    val useLocation: Boolean = false,
    val lat: Double? = null,
    val lng: Double? = null
)

class CheckoutViewModel(
    private val locationRepo: LocationRepository,
    private val orderRepo: OrderRepository,
    private val checkoutUseCase: CheckoutUseCase
) : ViewModel() {

    private val _ui = MutableStateFlow(CheckoutUi())
    val ui: StateFlow<CheckoutUi> = _ui

    private val _state = MutableStateFlow<UiState<Long>>(UiState.Success(-1))
    val state: StateFlow<UiState<Long>> = _state

    fun toggleUseLocation(v: Boolean) { _ui.value = _ui.value.copy(useLocation = v) }

    fun fetchLocation() {
        viewModelScope.launch {
            val res = locationRepo.getCurrentLocation()
            res.onSuccess { loc ->
                _ui.value = _ui.value.copy(lat = loc.latitude, lng = loc.longitude)
            }.onFailure {
                // ignore; UI may keep nulls
            }
        }
    }

    fun confirm(userId: Long, cart: List<CartItemWithItem>) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            val built = checkoutUseCase.build(cart)
            if (built.isFailure) {
                _state.value = UiState.Error(built.exceptionOrNull()?.message ?: "Checkout inválido")
                return@launch
            }
            val b = built.getOrThrow()
            val orderId = orderRepo.createOrder(
                userId = userId,
                orderType = b.orderType,
                total = b.total,
                lat = if (_ui.value.useLocation) _ui.value.lat else null,
                lng = if (_ui.value.useLocation) _ui.value.lng else null,
                lines = b.lines,
                reduceStock = b.reduceStock
            )
            _state.value = UiState.Success(orderId)
        }
    }
}
