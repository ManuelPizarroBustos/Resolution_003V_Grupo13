package com.techrent.app.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techrent.app.core.UiState
import com.techrent.app.data.local.model.OrderWithLines
import com.techrent.app.data.repository.OrderRepository
import com.techrent.app.domain.model.OrderStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminOrdersViewModel(private val repo: OrderRepository) : ViewModel() {

    val orders: StateFlow<UiState<List<OrderWithLines>>> =
        repo.observeAll()
            .map< List<OrderWithLines>, UiState<List<OrderWithLines>> > { UiState.Success(it) }
            .catch { emit(UiState.Error(it.message ?: "Error cargando pedidos")) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    fun order(orderId: Long): StateFlow<UiState<OrderWithLines>> =
        repo.observeById(orderId)
            .map { ow ->
                if (ow == null) UiState.Error("Orden #$orderId no encontrada")
                else UiState.Success(ow)
            }
            .catch { emit(UiState.Error(it.message ?: "Error cargando detalle")) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    fun updateStatus(orderId: Long, status: OrderStatus) {
        viewModelScope.launch { repo.updateStatus(orderId, status) }
    }
}
