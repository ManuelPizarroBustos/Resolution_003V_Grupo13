package com.techrent.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.techrent.app.data.local.dao.CartDao
import com.techrent.app.data.local.dao.ItemDao
import com.techrent.app.data.local.dao.OrderDao
import com.techrent.app.data.local.dao.UserDao
import com.techrent.app.data.local.entity.CartItemEntity
import com.techrent.app.data.local.entity.ItemEntity
import com.techrent.app.data.local.entity.OrderEntity
import com.techrent.app.data.local.entity.OrderLineEntity
import com.techrent.app.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, ItemEntity::class, CartItemEntity::class, OrderEntity::class, OrderLineEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TechRentDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun itemDao(): ItemDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        fun build(context: Context): TechRentDatabase =
            Room.databaseBuilder(context, TechRentDatabase::class.java, "techrent.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
