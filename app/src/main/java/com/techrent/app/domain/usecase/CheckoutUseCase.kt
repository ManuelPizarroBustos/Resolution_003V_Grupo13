package com.techrent.app.domain.usecase

import com.techrent.app.core.DateTime
import com.techrent.app.data.local.entity.OrderLineEntity
import com.techrent.app.data.local.model.CartItemWithItem
import com.techrent.app.domain.model.ItemType
import com.techrent.app.domain.model.OrderType

data class CheckoutResult(
    val orderType: OrderType,
    val total: Double,
    val lines: List<OrderLineEntity>,
    val reduceStock: List<Pair<Long, Int>>
)

class CheckoutUseCase {
    fun build(cart: List<CartItemWithItem>): Result<CheckoutResult> {
        if (cart.isEmpty()) return Result.failure(Exception("Carrito vacío"))

        val isRental = cart.any { it.item.type == ItemType.RENTAL }
        val orderType = if (isRental) OrderType.RENTAL else OrderType.PURCHASE

        var total = 0.0
        val lines = mutableListOf<OrderLineEntity>()
        val reduceStock = mutableListOf<Pair<Long, Int>>()

        for (c in cart) {
            val item = c.item
            val qty = c.cart.qty

            val (unitPrice, rentalDays) = when (item.type) {
                ItemType.RENTAL -> {
                    val start = DateTime.parseDateOrNull(c.cart.rentalStartDate ?: "")
                        ?: return Result.failure(Exception("Fecha inicio arriendo inválida"))
                    val end = DateTime.parseDateOrNull(c.cart.rentalEndDate ?: "")
                        ?: return Result.failure(Exception("Fecha fin arriendo inválida"))
                    val days = DateTime.daysBetween(start, end).toInt().coerceAtLeast(1)
                    val rate = item.dailyRate ?: return Result.failure(Exception("Arriendo sin tarifa diaria"))
                    (rate to days)
                }
                ItemType.SALE -> (item.price to null)
                ItemType.SERVICE -> (item.price to null)
            }

            val subtotal = unitPrice * qty * (rentalDays ?: 1)
            total += subtotal

            if (item.type == ItemType.SALE) reduceStock += (item.id to qty)

            lines += OrderLineEntity(
                orderId = 0,
                itemId = item.id,
                qty = qty,
                unitPrice = unitPrice,
                rentalDays = rentalDays,
                subtotal = subtotal
            )
        }

        return Result.success(CheckoutResult(orderType, total, lines, reduceStock))
    }
}
