package com.techrent.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.techrent.app.data.local.entity.OrderEntity
import com.techrent.app.data.local.entity.OrderLineEntity
import com.techrent.app.data.local.model.OrderWithLines
import com.techrent.app.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert
    suspend fun insertLines(lines: List<OrderLineEntity>)

    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeByUser(userId: Long): Flow<List<OrderWithLines>>

    @Transaction
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<OrderWithLines>>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    fun observeById(orderId: Long): Flow<OrderWithLines?>

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateStatus(orderId: Long, status: OrderStatus)
}
