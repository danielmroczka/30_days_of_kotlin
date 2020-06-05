package com.dm.labs.model

/**
 * Class represents Transaction made between Seller and Buyer when the price matches
 */
data class Transaction(val quantity: Int, val price: Int) {

    override fun toString(): String {
        return String.format("Transaction {price=%d$, quantity=%d}", price, quantity)
    }

}
