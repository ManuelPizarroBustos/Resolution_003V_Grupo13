package com.techrent.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techrent.app.domain.model.ItemType

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val type: ItemType,
    val price: Double,
    val dailyRate: Double?, // RENTAL
    val stock: Int,
    val isAvailable: Boolean,
    val imageUri: String?,
    val createdAt: Long
)
