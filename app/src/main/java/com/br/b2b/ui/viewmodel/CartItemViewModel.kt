package com.br.b2b.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.b2b.domain.model.CartItem
import com.br.b2b.domain.repository.CartItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartItemViewModel @Inject constructor(
    private val repository: CartItemRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow(emptyList<CartItem>())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _foundedCartItem = MutableStateFlow<CartItem?>(null)
    val foundedCartItem: StateFlow<CartItem?> = _foundedCartItem

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total

    private var lastUpdateTime = System.currentTimeMillis()

    init {
        calculateTotal()
        getAllItemsFromCart()
    }

    fun addToCart(cartItem: CartItem) {
        viewModelScope.launch {
            repository.addToCart(cartItem)
        }
    }

    fun removeFromCart(productId: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.removeFromCartByProductId(productId)
            calculateTotal()
            onComplete.invoke()
        }
    }

    fun calculateTotal() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                repository.calculateTotal()
            }.onSuccess {
                _total.tryEmit(it)
            }
        }
    }

    fun findCartItemsById(productId: Int) {
        viewModelScope.launch {
            runCatching {
                repository.getCartItemByProductId(productId)
            }.onSuccess {
                _foundedCartItem.tryEmit(it)
            }
        }
    }

    fun getAllItemsFromCart() {
        viewModelScope.launch {
            runCatching {
                repository.getAllCartItems()
            }.onSuccess {
                _cartItems.tryEmit(it)
                calculateTotal()
            }
        }
    }

    fun updateCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            repository.updateCartItem(cartItem)
        }
    }

    fun updateCartItemQuantity(productId: Int, newQuantity: Int) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime > 500) { // Tempo de debounce de 500 milissegundos
            viewModelScope.launch {
                repository.updateCartItemQuantity(productId, newQuantity)
                calculateTotal()
            }
            lastUpdateTime = currentTime
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            calculateTotal()
        }
    }
}