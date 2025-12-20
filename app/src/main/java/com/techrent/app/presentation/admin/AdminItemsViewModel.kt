package com.techrent.app.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techrent.app.core.UiState
import com.techrent.app.data.local.entity.ItemEntity
import com.techrent.app.data.repository.ItemRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminItemsViewModel(private val repo: ItemRepository) : ViewModel() {

    val items: StateFlow<UiState<List<ItemEntity>>> =
        repo.observeAll()
            .map< List<ItemEntity>, UiState<List<ItemEntity>> > { UiState.Success(it) }
            .catch { emit(UiState.Error(it.message ?: "Error cargando items")) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    fun delete(item: ItemEntity) {
        viewModelScope.launch { repo.delete(item) }
    }
}
