package com.br.b2b.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.br.b2b.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartItem: CartItem)

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItemByProductId(productId: Int): CartItem?

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItem>

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun removeFromCart(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun removeProductFromCart(productId: Int)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun removeFromCartByProductId(productId: Int)

    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE productId = :productId")
    suspend fun incrementQuantity(productId: Int)

    @Query("UPDATE cart_items SET quantity = quantity - 1 WHERE productId = :productId AND quantity > 1")
    suspend fun decrementQuantity(productId: Int)

    @Query("UPDATE cart_items SET quantity = :newQuantity WHERE productId = :productId")
    suspend fun updateCartItemQuantity(productId: Int, newQuantity: Int)

    @Query("SELECT COUNT(*) FROM cart_items WHERE productId = :productId")
    fun getProductQuantityInCart(productId: Int): Int

    @Query("SELECT SUM(unitPrice * quantity) FROM cart_items")
    fun calculateTotal(): Double

    @Query("SELECT SUM(quantity) FROM cart_items")
    fun calculateQuantity(): Int

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
