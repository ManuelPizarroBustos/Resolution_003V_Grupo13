package com.techrent.app.data.repository

import com.techrent.app.data.local.dao.CartDao
import com.techrent.app.data.local.dao.ItemDao
import com.techrent.app.data.local.dao.OrderDao
import com.techrent.app.data.local.entity.OrderEntity
import com.techrent.app.data.local.entity.OrderLineEntity
import com.techrent.app.data.local.model.OrderWithLines
import com.techrent.app.domain.model.ItemType
import com.techrent.app.domain.model.OrderStatus
import com.techrent.app.domain.model.OrderType
import kotlinx.coroutines.flow.Flow

class OrderRepository(
    private val orderDao: OrderDao,
    private val itemDao: ItemDao,
    private val cartDao: CartDao
) {
    fun observeByUser(userId: Long): Flow<List<OrderWithLines>> = orderDao.observeByUser(userId)
    fun observeAll(): Flow<List<OrderWithLines>> = orderDao.observeAll()
    fun observeById(orderId: Long): Flow<OrderWithLines?> = orderDao.observeById(orderId)

    suspend fun createOrder(
        userId: Long,
        orderType: OrderType,
        total: Double,
        lat: Double?,
        lng: Double?,
        lines: List<OrderLineEntity>,
        reduceStock: List<Pair<Long, Int>>
    ): Long {
        val orderId = orderDao.insertOrder(
            OrderEntity(
                userId = userId,
                createdAt = System.currentTimeMillis(),
                status = OrderStatus.PENDIENTE,
                total = total,
                lat = lat,
                lng = lng,
                orderType = orderType
            )
        )

        orderDao.insertLines(lines.map { it.copy(orderId = orderId) })

        for ((itemId, qty) in reduceStock) {
            val item = itemDao.findById(itemId) ?: continue
            if (item.type == ItemType.SALE) {
                itemDao.upsert(item.copy(stock = (item.stock - qty).coerceAtLeast(0)))
            }
        }

        cartDao.clear(userId)
        return orderId
    }

    suspend fun updateStatus(orderId: Long, status: OrderStatus) = orderDao.updateStatus(orderId, status)
}
