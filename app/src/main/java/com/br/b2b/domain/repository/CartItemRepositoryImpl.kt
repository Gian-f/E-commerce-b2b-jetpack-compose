package com.br.b2b.domain.repository

import com.br.b2b.data.local.dao.CartItemDao
import com.br.b2b.data.local.dao.OrderDao
import com.br.b2b.domain.model.CartItem
import com.br.b2b.domain.model.Order
import javax.inject.Inject

class CartItemRepositoryImpl @Inject constructor(
    private val cartItemDao: CartItemDao,
    private val orderDao: OrderDao
) : CartItemRepository {

    override suspend fun addToCart(cartItem: CartItem) {
        cartItemDao.addToCart(cartItem)
    }

    override suspend fun getCartItemByProductId(productId: Int): CartItem? {
        return cartItemDao.getCartItemByProductId(productId)
    }

    override suspend fun getAllCartItems(): List<CartItem> {
        return cartItemDao.getAllCartItems()
    }

    override suspend fun updateCartItem(cartItem: CartItem) {
        cartItemDao.updateCartItem(cartItem)
    }

    override suspend fun removeFromCart(cartItem: CartItem) {
        cartItemDao.removeFromCart(cartItem)
    }

    override suspend fun removeFromCartByProductId(productId: Int) {
        cartItemDao.removeFromCartByProductId(productId)
    }

    override suspend fun incrementQuantity(productId: Int) {
        cartItemDao.incrementQuantity(productId)
    }

    override suspend fun decrementQuantity(productId: Int) {
        cartItemDao.decrementQuantity(productId)
    }

    override suspend fun updateCartItemQuantity(productId: Int, newQuantity: Int) {
        cartItemDao.updateCartItemQuantity(productId, newQuantity)
    }

    override suspend fun getProductQuantityInCart(productId: Int): Int {
        return cartItemDao.getProductQuantityInCart(productId)
    }

    override suspend fun removeProductFromCart(productId: Int) {
        return cartItemDao.removeProductFromCart(productId)
    }

    override suspend fun calculateTotal(): Double {
        return cartItemDao.calculateTotal()
    }

    override suspend fun calculateQuantity(): Int {
        return cartItemDao.calculateQuantity()
    }


    override suspend fun clearCart() {
        cartItemDao.clearCart()
    }

    override suspend fun insertOrder(order: Order): Result<Unit> {
        return runCatching {
            orderDao.insertAll(order)
        }.onFailure {
            print(it.message)
        }
    }

    override suspend fun getAllOrders(): Result<List<Order>> {
        return runCatching {
            orderDao.getAllOrders()
        }
    }
}