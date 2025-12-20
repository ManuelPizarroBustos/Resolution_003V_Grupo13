package com.techrent.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techrent.app.domain.model.Role

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val passwordHash: String,
    val role: Role
)
