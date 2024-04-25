package com.br.b2b.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.b2b.domain.model.Category
import com.br.b2b.domain.model.Product
import com.br.b2b.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init {
        fetchProducts()
        fetchCategories()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            runCatching {
                repository.fetchAllProducts()
            }.onSuccess { result ->
                _products.value = result.getOrNull()?.result
            }.onFailure { error ->
                // Tratamento de erro, por exemplo, mostrando uma mensagem de erro para o usuário
                // ou atualizando um StateFlow de erro
                _products.value = null
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            runCatching {
                repository.fetchAllCategories()
            }.onSuccess { result ->
                _categories.value = result.getOrNull()?.result
            }.onFailure { error ->
                // Tratamento de erro, por exemplo, mostrando uma mensagem de erro para o usuário
                // ou atualizando um StateFlow de erro
                _categories.value = null
            }
        }
    }
}
