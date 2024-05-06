package com.br.b2b.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.b2b.domain.model.CartItem
import com.br.b2b.domain.repository.CartItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CartItemViewModel @Inject constructor(
    private val repository: CartItemRepository
) : ViewModel() {

    private val addToCartFlow = MutableSharedFlow<CartItem>()
    private val removeFromCartFlow = MutableSharedFlow<Int>()
    private val updateCartItemFlow = MutableSharedFlow<CartItem>()
    private val clearCartFlow = MutableSharedFlow<Unit>()

    private val _cartItems = MutableStateFlow(emptyList<CartItem>())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _foundedCartItem = MutableStateFlow<CartItem?>(null)
    val foundedCartItem: StateFlow<CartItem?> = _foundedCartItem

    init {
        viewModelScope.launch {
            addToCartFlow.debounce(1000).collect { cartItem ->
                repository.addToCart(cartItem)
            }
        }

        viewModelScope.launch {
            removeFromCartFlow.debounce(1000).collect { productId ->
                repository.removeFromCartByProductId(productId)
            }
        }

        viewModelScope.launch {
            updateCartItemFlow.debounce(1000).collect { cartItem ->
                repository.updateCartItem(cartItem)
            }
        }

        viewModelScope.launch {
            clearCartFlow.debounce(1000).collect {
                repository.clearCart()
            }
        }
    }

    fun addToCart(cartItem: CartItem) {
        viewModelScope.launch {
            addToCartFlow.emit(cartItem)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            removeFromCartFlow.emit(productId)
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
            }
        }
    }

    fun updateCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            updateCartItemFlow.emit(cartItem)
        }
    }

    fun updateCartItemQuantity(productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(productId, newQuantity)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartFlow.emit(Unit)
        }
    }
}