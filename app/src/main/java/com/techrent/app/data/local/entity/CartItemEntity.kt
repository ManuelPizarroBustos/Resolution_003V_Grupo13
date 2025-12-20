package com.techrent.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ItemEntity::class, parentColumns = ["id"], childColumns = ["itemId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("userId"), Index("itemId"), Index(value = ["userId","itemId"], unique = true)]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val itemId: Long,
    val qty: Int,
    val rentalStartDate: String?, // yyyy-MM-dd
    val rentalEndDate: String?
)
