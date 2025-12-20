package com.techrent.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_lines",
    foreignKeys = [
        ForeignKey(entity = OrderEntity::class, parentColumns = ["id"], childColumns = ["orderId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ItemEntity::class, parentColumns = ["id"], childColumns = ["itemId"], onDelete = ForeignKey.RESTRICT)
    ],
    indices = [Index("orderId"), Index("itemId")]
)
data class OrderLineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val itemId: Long,
    val qty: Int,
    val unitPrice: Double,
    val rentalDays: Int?,
    val subtotal: Double
)
