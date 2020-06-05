package com.dm.labs

import org.junit.Test

class OrderTest {

    @Test(expected = IllegalArgumentException::class)
    fun shouldNotAcceptInvalidQuantity() {
        Order(1, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldNotAcceptInvalidPrice() {
        Order(0, 1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldNotAcceptInvalidPriceAndQuantity() {
        Order(0, 0)
    }
}