package com.techrent.app.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.techrent.app.data.local.entity.CartItemEntity
import com.techrent.app.data.local.entity.ItemEntity

data class CartItemWithItem(
    @Embedded val cart: CartItemEntity,
    @Relation(parentColumn = "itemId", entityColumn = "id")
    val item: ItemEntity
)
