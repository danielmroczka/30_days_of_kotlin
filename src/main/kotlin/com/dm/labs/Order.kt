package com.dm.labs

/**
 * Class represents Order made buy Seller or Buyer
 */
class Order @JvmOverloads constructor(price: Int, quantity: Int, expiration: Long = Long.MAX_VALUE) {

    var quantity: Int

    /**
     * The bid price is what buyers are willing to pay for it.
     * The ask price is what sellers are willing to take for it.
     */
    val price: Int

    val timestamp: Long

    val expiration: Long

    override fun toString(): String {
        return String.format("{price=%d, quantity=%d}", price, quantity)
    }

    init {
        require(quantity > 0 && price > 0) { "At least one of the argument is not correct!" }
        this.price = price
        this.quantity = quantity
        this.expiration = expiration
        timestamp = System.nanoTime()
    }
}
