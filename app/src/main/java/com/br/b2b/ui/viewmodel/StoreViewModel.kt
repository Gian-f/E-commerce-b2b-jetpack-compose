package com.br.b2b.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.b2b.data.local.converter.convertProduct
import com.br.b2b.domain.model.Category
import com.br.b2b.domain.model.Product
import com.br.b2b.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val repository: StoreRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>?>(null)
    val products: StateFlow<List<Product>?> = _products

    private val _categories = MutableStateFlow<List<Category>?>(null)
    val categories: StateFlow<List<Category>?> = _categories

    private val _foundProduct = MutableStateFlow<Product?>(null)
    val foundProduct: StateFlow<Product?> = _foundProduct

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init {
        fetchCategories()
        fetchProducts()
    }

    fun findProductById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.findProductById(id)
            if (result.isSuccess) {
                _foundProduct.value = result.getOrNull()
            } else {
                _errorMessage.value = "Erro ao buscar o produto: ${result.exceptionOrNull()?.localizedMessage}"
            }
        }
    }

    fun fetchProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                repository.fetchAllProducts()
            }.onSuccess { result ->
                _products.value = result.getOrNull()?.result?.let { convertProduct(it) }
            }.onFailure { error ->
                _errorMessage.value = "Erro ao buscar os produtos: ${error.localizedMessage}"
                _products.value = null
            }
        }
    }

    fun fetchCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                repository.fetchAllCategories()
            }.onSuccess { result ->
                _categories.value = result.getOrNull()?.result
            }.onFailure { error ->
                _errorMessage.value = "Erro ao buscar os produtos: ${error.localizedMessage}"
                _categories.value = null
            }
        }
    }
}
