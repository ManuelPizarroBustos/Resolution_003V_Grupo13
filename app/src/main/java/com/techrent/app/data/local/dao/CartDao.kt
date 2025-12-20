package com.techrent.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.techrent.app.data.local.entity.CartItemEntity
import com.techrent.app.data.local.model.CartItemWithItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Transaction
    @Query("SELECT * FROM cart_items WHERE userId = :userId ORDER BY id DESC")
    fun observeCart(userId: Long): Flow<List<CartItemWithItem>>

    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    fun observeCartCount(userId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cart: CartItemEntity): Long

    @Query("SELECT * FROM cart_items WHERE userId=:userId AND itemId=:itemId LIMIT 1")
    suspend fun find(userId: Long, itemId: Long): CartItemEntity?

    @Update
    suspend fun update(cart: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clear(userId: Long)
}
