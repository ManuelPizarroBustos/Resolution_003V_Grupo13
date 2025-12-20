package com.techrent.app.presentation.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techrent.app.core.UiState
import com.techrent.app.data.local.model.CartItemWithItem
import com.techrent.app.data.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(private val repo: CartRepository) : ViewModel() {
    fun cart(userId: Long): StateFlow<UiState<List<CartItemWithItem>>> =
        repo.observeCart(userId)
            .map { UiState.Success(it) as UiState<List<CartItemWithItem>> }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    fun count(userId: Long) = repo.observeCount(userId)

    fun inc(userId: Long, itemId: Long, currentQty: Int) {
        viewModelScope.launch { repo.updateQty(userId, itemId, currentQty + 1) }
    }

    fun dec(userId: Long, itemId: Long, currentQty: Int) {
        val newQty = (currentQty - 1).coerceAtLeast(1)
        viewModelScope.launch { repo.updateQty(userId, itemId, newQty) }
    }

    fun remove(cartId: Long) {
        viewModelScope.launch { repo.remove(cartId) }
    }
}
