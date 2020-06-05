package com.dm.labs.service

import com.dm.labs.model.Order
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class OrderServiceTest {
    private var orderService: OrderService? = null

    @Before
    fun setUp() {
        orderService = OrderService()
    }

    @Test
    fun shouldCreateBuyOrder() {
        //GIVEN
        //WHEN
        orderService?.buyOrder(Order(100, 100))
        //THEN
        assertEquals(1, orderService?.buyOrders?.size)
        assertEquals(0, orderService?.sellOrders?.size)
    }

    @Test
    fun shouldCreateManyBuyOrders() {
        //GIVEN
        //WHEN
        orderService?.buyOrder(Order(100, 100))
        orderService?.buyOrder(Order(100, 100))
        orderService?.buyOrder(Order(100, 100))
        //THEN
        assertEquals(3, orderService?.buyOrders?.size)
        assertEquals(0, orderService?.sellOrders?.size)
    }

    @Test
    fun shouldCreateSellOrder() {
        //GIVEN
        //WHEN
        orderService?.sellOrder(Order(100, 100))
        //THEN
        assertEquals(0, orderService?.buyOrders?.size)
        assertEquals(1, orderService?.sellOrders?.size)
    }

    @Test
    fun shouldCreateManySellOrders() {
        //GIVEN
        //WHEN
        orderService?.sellOrder(Order(100, 100))
        orderService?.sellOrder(Order(100, 100))
        orderService?.sellOrder(Order(100, 100))
        //THEN
        assertEquals(0, orderService?.buyOrders?.size)
        assertEquals(3, orderService?.sellOrders?.size)
    }

    @Test
    fun shouldSellOrderSortedAscending() {
        //GIVEN
        //WHEN
        orderService?.sellOrder(Order(110, 100))
        orderService?.sellOrder(Order(105, 100))
        orderService?.sellOrder(Order(100, 100))
        //THEN
        val list: List<Order> = ArrayList<Order>(orderService?.sellOrders)
        assertEquals(100, list[0].price)
        assertEquals(110, list[2].price)
    }

    @Test
    fun shouldBuyOrderSortedDescending() {
        //GIVEN
        //WHEN
        orderService?.buyOrder(Order(105, 100))
        orderService?.buyOrder(Order(110, 100))
        orderService?.buyOrder(Order(100, 100))
        //THEN
        val list: List<Order> = ArrayList<Order>(orderService?.buyOrders)
        assertEquals(110, list[0].price)
        assertEquals(100, list[2].price)
    }

    @Test
    fun shouldCancelOrder() {
        //GIVEN
        val order1 = orderService!!.buyOrder(Order(100, 100))
        val order2 = orderService!!.sellOrder(Order(200, 100))
        //WHEN
        orderService?.cancelOrder(order1)
        orderService?.cancelOrder(order2)
        //THEN
        assertEquals(0, orderService?.buyOrders?.size)
        assertEquals(0, orderService?.sellOrders?.size)
        assertEquals(0, orderService?.transactions?.size)
    }

    @Test
    fun scenarioSimpleMatch() {
        //GIVEN
        orderService?.buyOrder(Order(100, 100))
        orderService?.sellOrder(Order(100, 100))
        //WHEN

        //THEN
        assertEquals(0, orderService?.buyOrders?.size)
        assertEquals(0, orderService?.sellOrders?.size)
        assertEquals(1, orderService?.transactions?.size)
        assertEquals(100, orderService?.transactions?.get(0)?.price)
        assertEquals(100, orderService?.transactions?.get(0)?.quantity)
    }

    @Test
    fun scenarioSimpleOneNotMatched() {
        //GIVEN
        //WHEN
        orderService?.buyOrder(Order(100, 100))
        orderService?.sellOrder(Order(120, 100))
        //THEN
        assertEquals(1, orderService?.buyOrders?.size)
        assertEquals(1, orderService?.sellOrders?.size)
        assertEquals(0, orderService?.transactions?.size)
    }

    @Test
    fun scenarioEqualBids() {
        //GIVEN
        orderService?.buyOrder(Order(100, 1))
        orderService?.buyOrder(Order(105, 2))
        orderService?.buyOrder(Order(110, 3))
        orderService?.sellOrder(Order(110, 3))
        orderService?.sellOrder(Order(105, 2))
        orderService?.sellOrder(Order(100, 1))
        //WHEN

        //THEN
        assertEquals(0, orderService?.buyOrders?.size)
        assertEquals(0, orderService?.sellOrders?.size)
        assertEquals(3, orderService?.transactions?.size)
        assertEquals(110, orderService?.transactions?.get(0)?.price)
        assertEquals(3, orderService?.transactions?.get(0)?.quantity)
        assertEquals(105, orderService?.transactions?.get(1)?.price)
        assertEquals(2, orderService?.transactions?.get(1)?.quantity)
        assertEquals(100, orderService?.transactions?.get(2)?.price)
        assertEquals(1, orderService?.transactions?.get(2)?.quantity)
    }

    @Test
    fun scenarioOneBuysManySell() {
        //GIVEN
        orderService?.buyOrder(Order(200, 600))
        orderService?.sellOrder(Order(100, 100))
        orderService?.sellOrder(Order(150, 200))
        orderService?.sellOrder(Order(200, 300))
        //WHEN

        //THEN
        assertEquals(0, orderService?.buyOrders?.size)
        assertEquals(0, orderService?.sellOrders?.size)
        assertEquals(3, orderService?.transactions?.size)
        assertEquals(100, orderService?.transactions?.get(0)?.price)
        assertEquals(100, orderService?.transactions?.get(0)?.quantity)
        assertEquals(150, orderService?.transactions?.get(1)?.price)
        assertEquals(200, orderService?.transactions?.get(1)?.quantity)
        assertEquals(200, orderService?.transactions?.get(2)?.price)
        assertEquals(300, orderService?.transactions?.get(2)?.quantity)
    }

    @Test
    fun scenarioOneSellsManyBuy() {
        //GIVEN
        orderService?.sellOrder(Order(100, 600))
        orderService?.buyOrder(Order(200, 100))
        orderService?.buyOrder(Order(150, 200))
        orderService?.buyOrder(Order(100, 300))
        //WHEN

        //THEN
        assertEquals(0, orderService?.sellOrders?.size)
        assertEquals(0, orderService?.buyOrders?.size)
        assertEquals(3, orderService?.transactions?.size)
        assertEquals(100, orderService?.transactions?.get(0)?.price)
        assertEquals(100, orderService?.transactions?.get(0)?.quantity)
        assertEquals(100, orderService?.transactions?.get(1)?.price)
        assertEquals(200, orderService?.transactions?.get(1)?.quantity)
        assertEquals(100, orderService?.transactions?.get(2)?.price)
        assertEquals(300, orderService?.transactions?.get(2)?.quantity)
    }

    @Test
    fun scenarioNothingShouldBeSold() {
        //GIVEN
        //WHEN
        orderService?.sellOrder(Order(200, 100))
        orderService?.buyOrder(Order(100, 100))
        orderService?.buyOrder(Order(100, 100))
        orderService?.buyOrder(Order(100, 100))
        //THEN
        assertEquals(1, orderService?.sellOrders?.size)
        assertEquals(3, orderService?.buyOrders?.size)
        assertEquals(0, orderService?.transactions?.size)
    }

    @Test
    fun scenarioBuyOrderLeft() {
        //GIVEN
        //WHEN
        orderService?.sellOrder(Order(100, 50))
        orderService?.sellOrder(Order(110, 60))
        orderService?.buyOrder(Order(120, 70))
        orderService?.buyOrder(Order(110, 100))

        //THEN
        assertEquals(0, orderService?.sellOrders?.size)
        assertEquals(1, orderService?.buyOrders?.size)
        assertEquals(3, orderService?.transactions?.size)
        assertEquals(100, orderService?.transactions?.get(0)?.price)
        assertEquals(50, orderService?.transactions?.get(0)?.quantity)
        assertEquals(110, orderService?.transactions?.get(1)?.price)
        assertEquals(20, orderService?.transactions?.get(1)?.quantity)
        assertEquals(110, orderService?.transactions?.get(2)?.price)
        assertEquals(40, orderService?.transactions?.get(2)?.quantity)
    }

    @Test
    fun scenarioSellOrderLeft() {
        //GIVEN
        //WHEN
        orderService?.buyOrder(Order(100, 50))
        orderService?.buyOrder(Order(110, 60))
        orderService?.sellOrder(Order(120, 70))
        orderService?.sellOrder(Order(90, 110))

        //THEN
        assertEquals(1, orderService?.sellOrders?.size)
        assertEquals(0, orderService?.buyOrders?.size)
        assertEquals(2, orderService?.transactions?.size)
        assertEquals(90, orderService?.transactions?.get(0)?.price)
        assertEquals(60, orderService?.transactions?.get(0)?.quantity)
        assertEquals(90, orderService?.transactions?.get(1)?.price)
        assertEquals(50, orderService?.transactions?.get(1)?.quantity)
    }

    @Test
    fun expiredOrdersShouldNotBeConsider() {
        //GIVEN
        // WHEN
        orderService?.buyOrder(Order(100, 100, System.currentTimeMillis() - 1))
        orderService?.sellOrder(Order(100, 100, System.currentTimeMillis() - 1))
        //THEN
        assertEquals(0, orderService?.buyOrders?.size)
        assertEquals(0, orderService?.sellOrders?.size)
        assertEquals(0, orderService?.transactions?.size)
    }
}