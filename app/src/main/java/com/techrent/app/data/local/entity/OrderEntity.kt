package com.techrent.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.techrent.app.domain.model.OrderStatus
import com.techrent.app.domain.model.OrderType

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("userId")]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val createdAt: Long,
    val status: OrderStatus,
    val total: Double,
    val lat: Double?,
    val lng: Double?,
    val orderType: OrderType
)
