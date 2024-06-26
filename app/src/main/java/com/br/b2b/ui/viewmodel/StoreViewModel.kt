package com.br.b2b.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.b2b.data.local.datastore.DataStoreManager
import com.br.b2b.domain.model.CartItem
import com.br.b2b.domain.model.Category
import com.br.b2b.domain.model.Product
import com.br.b2b.domain.repository.CartItemRepository
import com.br.b2b.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val repository: StoreRepository,
    private val cartItemRepository: CartItemRepository,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>?>(null)
    val products: StateFlow<List<Product>?> = _products

    private val _recommendedProducts = MutableStateFlow<List<Product>?>(null)
    val recommendedProducts: StateFlow<List<Product>?> = _recommendedProducts

    private val _eletronicsProducts = MutableStateFlow<List<Product>?>(null)
    val eletronicsProducts: StateFlow<List<Product>?> = _eletronicsProducts

    private val _filteredProducts = MutableStateFlow<List<Product>?>(null)
    val filteredProducts: StateFlow<List<Product>?> = _filteredProducts

    private val _categories = MutableStateFlow<List<Category>?>(null)
    val categories: StateFlow<List<Category>?> = _categories

    private val _foundProduct = MutableStateFlow<Product?>(null)
    val foundProduct: StateFlow<Product?> = _foundProduct

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _active = MutableStateFlow(false)
    val active: StateFlow<Boolean> = _active

    private val _searchHistory = MutableStateFlow(emptyList<String>())
    val searchHistory: Flow<List<String>> = _searchHistory

    init {
        getSearchHistory()
    }

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun findProducts(term: String) {
        _isLoading.value = true
        viewModelScope.launch {
            runCatching {
                repository.findProducts(term)
            }.onSuccess {
                _filteredProducts.value = it.getOrThrow().filterNotNull()
                delay(2000)
                _isLoading.value = false
            }.onFailure {
                _errorMessage.value = it.message.orEmpty()
                delay(2000)
                _isLoading.value = false
            }
        }
    }

    fun toggleSearchBar() {
        if (_active.value) {
            clearFilteredProducts()
        }
        _active.value = !_active.value
    }

    fun clearFilteredProducts() {
        _filteredProducts.value = emptyList()
    }

    fun saveSearchHistory(query: String) {
        viewModelScope.launch {
            dataStoreManager.updateSearchHistory(
                searchTerm = query,
                onSuccess = {
                    getSearchHistory()
                }
            )
        }
    }

    private fun getSearchHistory() {
        viewModelScope.launch {
            val result = dataStoreManager.getSearchHistory()
            _searchHistory.value = result
        }
    }

    fun addProductToCart(
        product: Product,
        quantity: Int = 1,
    ) {
        viewModelScope.launch {
            cartItemRepository.addToCart(
                CartItem(
                    productId = product.id,
                    productTitle = product.title,
                    quantity = quantity,
                    totalPrice = product.price,
                    currentStock = 100,
                    estimatedDeliveryDate = LocalDate.now().plusDays(3)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE),
                    productCategoryId = product.categoryId,
                    productImages = product.images,
                    productReviews = "",
                    promotions = "",
                    productDetails = "",
                    returnPolicy = "",
                    shippingFees = 0.0,
                    unitPrice = product.price,
                    warrantyInfo = "Garantia de 6 meses!",
                )
            )
        }
    }

    fun removeProductFromCart(productId: Int) {
        viewModelScope.launch {
            cartItemRepository.removeProductFromCart(productId)
        }
    }

    fun incrementToCart(product: Product) {
        viewModelScope.launch {
            cartItemRepository.incrementQuantity(product.id)
        }
    }

    fun decrementProductFromCart(product: Product) {
        viewModelScope.launch {
            cartItemRepository.decrementQuantity(product.id)
        }
    }

    fun findProductById(id: Int) {
        viewModelScope.launch {
            val result = repository.findProductById(id)
            if (result.isSuccess) {
                _foundProduct.value = result.getOrNull()
            } else {
                _errorMessage.value =
                    "Erro ao buscar o produto: ${result.exceptionOrNull()?.localizedMessage}"
            }
        }
    }

    fun fetchProducts() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.fetchAllProducts()
            if (result.isSuccess) {
                _products.value = result.getOrNull()
                result.getOrNull()?.let { repository.createProducts(it) }
                _isLoading.value = false
            } else {
                _errorMessage.value =
                    "Erro ao buscar os produtos: ${result.exceptionOrNull()?.localizedMessage}"
                _products.value = null
                _isLoading.value = false
            }
        }
    }

    fun fetchRecommendedProducts() {
        viewModelScope.launch {
            val result = repository.fetchAllRecommendedProducts()
            if (result.isSuccess) {
                _recommendedProducts.value = result.getOrNull()
            } else {
                _errorMessage.value =
                    "Erro ao buscar os produtos: ${result.exceptionOrNull()?.localizedMessage}"
                _recommendedProducts.value = null
            }
        }
    }

    fun fetchEletronicsProducts() {
        viewModelScope.launch {
            val result = repository.getProductsInCategory(1)
            if (result.isSuccess) {
                _eletronicsProducts.value = result.getOrNull()
            } else {
                _errorMessage.value =
                    "Erro ao buscar os produtos: ${result.exceptionOrNull()?.localizedMessage}"
                _eletronicsProducts.value = null
            }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            val result = repository.fetchAllCategories()
            if (result.isSuccess) {
                _categories.value = result.getOrNull()
                repository.createCategories(result.getOrThrow())
            } else {
                _errorMessage.value =
                    "Erro ao buscar as categorias: ${result.exceptionOrNull()?.localizedMessage}"
                _categories.value = null
            }
        }
    }

    fun toggleFavoriteStatus(productId: Int) {
        viewModelScope.launch {
            repository.toggleFavoriteStatus(productId)
        }
    }

    fun getProductsInCategory(categoryId: Int) {
        viewModelScope.launch {
            val result = repository.getProductsInCategory(categoryId)
            if (result.isSuccess) {
                _products.value = result.getOrNull()
            } else {
                _errorMessage.value =
                    "Erro ao buscar produtos na categoria: ${result.exceptionOrNull()?.localizedMessage}"
                _products.value = null
            }
        }
    }

    fun deleteAllProductsAndCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAllProducts()
            repository.deleteAllCategories()
        }
    }
}
