package com.techrent.app.data.repository

import com.techrent.app.data.local.dao.ItemDao
import com.techrent.app.data.local.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val dao: ItemDao) {
    fun observeAll(): Flow<List<ItemEntity>> = dao.observeAll()
    suspend fun findById(id: Long) = dao.findById(id)
    suspend fun upsert(item: ItemEntity): Long = dao.upsert(item)
    suspend fun delete(item: ItemEntity) = dao.delete(item)
    suspend fun updateInventory(id: Long, stock: Int, available: Boolean) = dao.updateInventory(id, stock, available)
}
