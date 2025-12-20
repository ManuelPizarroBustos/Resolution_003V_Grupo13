package com.techrent.app.presentation.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techrent.app.core.UiState
import com.techrent.app.data.local.model.OrderWithLines
import com.techrent.app.data.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(private val repo: OrderRepository) : ViewModel() {

    fun orders(userId: Long): StateFlow<UiState<List<OrderWithLines>>> =
        repo.observeByUser(userId)
            .map< List<OrderWithLines>, UiState<List<OrderWithLines>> > { UiState.Success(it) }
            .catch { emit(UiState.Error(it.message ?: "Error cargando historial")) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    fun order(orderId: Long): StateFlow<UiState<OrderWithLines>> =
        repo.observeById(orderId)
            .map { ow ->
                if (ow == null) UiState.Error("Orden #$orderId no encontrada")
                else UiState.Success(ow)
            }
            .catch { emit(UiState.Error(it.message ?: "Error cargando orden")) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)
}
