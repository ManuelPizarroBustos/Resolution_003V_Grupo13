package com.techrent.app.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.techrent.app.data.local.entity.OrderEntity
import com.techrent.app.data.local.entity.OrderLineEntity

data class OrderWithLines(
    @Embedded val order: OrderEntity,
    @Relation(parentColumn = "id", entityColumn = "orderId")
    val lines: List<OrderLineEntity>
)
