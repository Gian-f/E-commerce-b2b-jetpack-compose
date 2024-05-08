package com.br.b2b.domain.repository

import com.br.b2b.domain.model.CartItem

interface CartItemRepository {
    suspend fun addToCart(cartItem: CartItem)
    suspend fun getCartItemByProductId(productId: Int): CartItem?
    suspend fun getAllCartItems(): List<CartItem>
    suspend fun updateCartItem(cartItem: CartItem)
    suspend fun removeFromCart(cartItem: CartItem)
    suspend fun removeFromCartByProductId(productId: Int)
    suspend fun incrementQuantity(productId: Int)
    suspend fun decrementQuantity(productId: Int)
    suspend fun updateCartItemQuantity(productId: Int, newQuantity: Int)
    suspend fun getProductQuantityInCart(productId: Int): Int
    suspend fun removeProductFromCart(productId: Int)
    suspend fun calculateTotal(): Double
    suspend fun calculateQuantity(): Int
    suspend fun clearCart()
}