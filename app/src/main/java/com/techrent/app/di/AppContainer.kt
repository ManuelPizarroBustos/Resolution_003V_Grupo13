package com.techrent.app.di

import android.content.Context
import com.techrent.app.core.Crypto
import com.techrent.app.core.DateTime
import com.techrent.app.data.datastore.SessionStore
import com.techrent.app.data.local.TechRentDatabase
import com.techrent.app.data.local.entity.ItemEntity
import com.techrent.app.data.local.entity.UserEntity
import com.techrent.app.data.location.LocationRepository
import com.techrent.app.data.repository.AuthRepository
import com.techrent.app.data.repository.CartRepository
import com.techrent.app.data.repository.ItemRepository
import com.techrent.app.data.repository.OrderRepository
import com.techrent.app.domain.model.ItemType
import com.techrent.app.domain.model.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppContainer(context: Context) {
    private val db = TechRentDatabase.build(context)

    val sessionStore = SessionStore(context)

    val authRepository = AuthRepository(db.userDao(), sessionStore)
    val itemRepository = ItemRepository(db.itemDao())
    val cartRepository = CartRepository(db.cartDao(), db.itemDao())
    val orderRepository = OrderRepository(db.orderDao(), db.itemDao(), db.cartDao())
    val locationRepository = LocationRepository(context)

    fun seedIfNeeded() {
        CoroutineScope(Dispatchers.IO).launch {
            val already = sessionStore.seeded.first()
            if (already) return@launch

            if (db.userDao().countUsers() == 0) {
                db.userDao().insert(
                    UserEntity(
                        email = "admin@demo.cl",
                        passwordHash = Crypto.sha256("Admin123!"),
                        role = Role.ADMIN
                    )
                )
                db.userDao().insert(
                    UserEntity(
                        email = "cliente@demo.cl",
                        passwordHash = Crypto.sha256("Cliente123!"),
                        role = Role.CLIENT
                    )
                )
            }

            val now = DateTime.nowMillis()
            itemRepository.upsert(
                ItemEntity(
                    name = "PlayStation 5 (Venta)",
                    description = "Consola nueva, incluye 1 control.",
                    type = ItemType.SALE,
                    price = 549990.0,
                    dailyRate = null,
                    stock = 5,
                    isAvailable = true,
                    imageUri = null,
                    createdAt = now
                )
            )
            itemRepository.upsert(
                ItemEntity(
                    name = "Notebook Gamer (Arriendo)",
                    description = "RTX, ideal para gaming y render.",
                    type = ItemType.RENTAL,
                    price = 0.0,
                    dailyRate = 19990.0,
                    stock = 2,
                    isAvailable = true,
                    imageUri = null,
                    createdAt = now - 1000
                )
            )
            itemRepository.upsert(
                ItemEntity(
                    name = "Servicio: Limpieza de consola",
                    description = "Mantención + pasta térmica (según modelo).",
                    type = ItemType.SERVICE,
                    price = 24990.0,
                    dailyRate = null,
                    stock = 9999,
                    isAvailable = true,
                    imageUri = null,
                    createdAt = now - 2000
                )
            )

            sessionStore.setSeeded()
        }
    }
}
