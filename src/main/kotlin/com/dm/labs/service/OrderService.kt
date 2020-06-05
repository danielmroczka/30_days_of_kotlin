package com.dm.labs.service

import com.dm.labs.model.Order
import com.dm.labs.model.Transaction
import java.util.*
import kotlin.math.min

internal class OrderService {
    // Buy orders sorted descending by price and ascending by creation time
    private val buyOrderComparator = compareByDescending<Order> { it.price }.thenBy { it.timestamp }

    // Sell orders sorted ascending by price and ascending by creation time
    private val sellOrderComparator = compareBy<Order> { it.price }.thenBy { it.timestamp }

    val buyOrders: MutableSet<Order> = TreeSet(buyOrderComparator)
    val sellOrders: MutableSet<Order> = TreeSet(sellOrderComparator)
    val transactions: ArrayList<Transaction> = ArrayList()

    fun buyOrder(order: Order): Order {
        println("- Buy offer $order")
        buyOrders.add(order)
        match()
        return order
    }

    fun sellOrder(order: Order): Order {
        println("+ Sell offer $order")
        sellOrders.add(order)
        match()
        return order
    }

    fun cancelOrder(order: Order) {
        buyOrders.remove(order)
        sellOrders.remove(order)
    }

    fun printReport() {
        buyOrders.forEach(System.out::println)
        sellOrders.forEach(System.out::println)
        transactions.forEach(System.out::println)
    }

    private fun match() {
        // Don't trigger matching logic if there is not at least one buy and sell order
        if (buyOrders.isEmpty() || sellOrders.isEmpty()) {
            return
        }
        removeExpiredOrders(buyOrders)
        removeExpiredOrders(sellOrders)
        val ordersToClose: MutableList<Order> = ArrayList()

        val buys = buyOrders.toList()
        val sells = sellOrders.toList()

        var buyId = 0
        var sellId = 0

        while (buyId < buys.size && sellId < sells.size) {
            val buy = buys[buyId]
            val sell = sells[sellId]
            /**
             * Sell offers are sorted ascending, buy offers are sorted descending.
             * If once sell price is higher than buy price it doesn't make sense to iterate further.
             */
            if (sell.price > buy.price) {
                break
            }

            val quantity = min(sell.quantity, buy.quantity)
            val price = sell.price

            if (sell.quantity > quantity) {
                // 1. Seller has more to sell in order
                sell.quantity = sell.quantity - quantity
            } else {
                // 2. Seller has sold everything, order must be removed and the next order should be taken
                ordersToClose.add(sell)
                sellId++
            }

            if (buy.quantity > quantity) {
                // 3. Buyer has more to buy in order
                buy.quantity = buy.quantity - quantity
            } else {
                // 4 Buyer has bought everything, order must be removed and the next order should be taken
                ordersToClose.add(buy)
                buyId++
            }
            createTransaction(quantity, price)
        }
        ordersToClose.forEach { order -> cancelOrder(order) }
    }

    private fun removeExpiredOrders(orders: MutableSet<Order>) {
        orders.removeIf { o -> o.expiration < System.currentTimeMillis() }
    }

    private fun createTransaction(availableQuantity: Int, price: Int) {
        val transaction = Transaction(availableQuantity, price)
        transactions.add(transaction)
        println("= $transaction")
    }
}
