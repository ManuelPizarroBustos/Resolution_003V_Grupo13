package com.techrent.app.presentation.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techrent.app.core.UiState
import com.techrent.app.data.local.entity.ItemEntity
import com.techrent.app.data.repository.ItemRepository
import com.techrent.app.domain.model.ItemType
import kotlinx.coroutines.flow.*

data class HomeFilter(val query: String = "", val type: ItemType? = null)

class HomeViewModel(private val repo: ItemRepository) : ViewModel() {
    private val _filter = MutableStateFlow(HomeFilter())
    val filter: StateFlow<HomeFilter> = _filter

    val uiState: StateFlow<UiState<List<ItemEntity>>> =
        combine(repo.observeAll(), _filter) { items, f ->
            val q = f.query.trim().lowercase()
            items
                .filter { f.type == null || it.type == f.type }
                .filter { q.isEmpty() || it.name.lowercase().contains(q) || it.description.lowercase().contains(q) }
        }
            .map<List<ItemEntity>, UiState<List<ItemEntity>>> { UiState.Success(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    fun onQuery(q: String) { _filter.value = _filter.value.copy(query = q) }
    fun onType(t: ItemType?) { _filter.value = _filter.value.copy(type = t) }
}
