package com.dm.labs

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Welcome to Trade Service!")
        println("Service is ready for accepting the offers")
        val orderService = OrderService()

        orderService.sellOrder(Order(price = 100, quantity = 20))
        orderService.sellOrder(Order(price = 200, quantity = 20))

        orderService.buyOrder(Order(price = 100, quantity = 10))
        orderService.buyOrder(Order(price = 150, quantity = 10))
        orderService.buyOrder(Order(price = 200, quantity = 10))
        orderService.buyOrder(Order(price = 300, quantity = 10))
        orderService.buyOrder(Order(price = 300, quantity = 10))
    }

}