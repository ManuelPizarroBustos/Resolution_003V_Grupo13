package com.techrent.app.data.repository

import com.techrent.app.data.local.dao.CartDao
import com.techrent.app.data.local.dao.ItemDao
import com.techrent.app.data.local.entity.CartItemEntity
import com.techrent.app.data.local.model.CartItemWithItem
import com.techrent.app.domain.model.ItemType
import kotlinx.coroutines.flow.Flow

class CartRepository(
    private val cartDao: CartDao,
    private val itemDao: ItemDao
) {
    fun observeCart(userId: Long): Flow<List<CartItemWithItem>> = cartDao.observeCart(userId)
    fun observeCount(userId: Long): Flow<Int> = cartDao.observeCartCount(userId)

    suspend fun addOrIncrement(
        userId: Long,
        itemId: Long,
        qty: Int,
        rentalStart: String?,
        rentalEnd: String?
    ): Result<Unit> {
        if (userId <= 0) return Result.failure(Exception("Sesión no válida"))
        val item = itemDao.findById(itemId) ?: return Result.failure(Exception("Ítem no existe"))

        if (!item.isAvailable) return Result.failure(Exception("Ítem no disponible"))
        if (item.type == ItemType.SALE && item.stock <= 0) return Result.failure(Exception("Sin stock"))

        val existing = cartDao.find(userId, itemId)
        val newQty = (existing?.qty ?: 0) + qty

        if (item.type == ItemType.SALE && newQty > item.stock) {
            return Result.failure(Exception("Cantidad supera stock"))
        }

        return try {
            if (existing == null) {
                cartDao.insert(
                    CartItemEntity(
                        userId = userId,
                        itemId = itemId,
                        qty = qty,
                        rentalStartDate = rentalStart,
                        rentalEndDate = rentalEnd
                    )
                )
            } else {
                cartDao.update(existing.copy(qty = newQty, rentalStartDate = rentalStart, rentalEndDate = rentalEnd))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateQty(userId: Long, itemId: Long, qty: Int): Result<Unit> {
        if (userId <= 0) return Result.failure(Exception("Sesión no válida"))
        if (qty <= 0) return Result.failure(Exception("Cantidad inválida"))
        val item = itemDao.findById(itemId) ?: return Result.failure(Exception("Ítem no existe"))
        val current = cartDao.find(userId, itemId) ?: return Result.failure(Exception("Carrito no existe"))
        if (item.type == ItemType.SALE && qty > item.stock) return Result.failure(Exception("Supera stock"))
        return try {
            cartDao.update(current.copy(qty = qty))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun remove(cartId: Long) = cartDao.deleteById(cartId)
    suspend fun clear(userId: Long) = cartDao.clear(userId)
}
