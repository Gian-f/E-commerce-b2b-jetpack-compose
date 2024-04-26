package com.br.b2b.domain.repository

import com.br.b2b.data.local.converter.convertProduct
import com.br.b2b.data.local.dao.CategoryDao
import com.br.b2b.data.local.dao.ProductDao
import com.br.b2b.data.remote.dto.response.CategoryResponse
import com.br.b2b.data.remote.dto.response.ProductResponse
import com.br.b2b.data.remote.service.ApiService
import com.br.b2b.domain.model.Category
import com.br.b2b.domain.model.Product
import com.br.b2b.domain.model.ProductData
import javax.inject.Inject

class StoreRepositoryImpl
@Inject constructor(
    private val service: ApiService,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
) : StoreRepository {

    override suspend fun fetchAllProducts(): Result<ProductResponse?> {
        return runCatching {
            val response = service.fetchAllProducts()
            if (response.isSuccessful) {
                val productResponse = response.body()
                if (productResponse != null) {
                    val products = convertProduct(productResponse.result)
                    createProducts(products)
                    productResponse
                } else {
                    throw Exception("Erro ao trazer produtos: Resposta vazia")
                }
            } else {
                throw Exception("Erro ao trazer produtos: ${response.message()}")
            }
        }
    }

    override suspend fun fetchAllCategories(): Result<CategoryResponse?> {
        return runCatching {
            val response = service.fetchAllCategories()
            if (response.isSuccessful) {
                createCategories(response.body()?.result ?: emptyList())
                response.body()
            } else {
                throw Exception("Erro ao trazer categorias: ${response.message()}")
            }
        }
    }

    override suspend fun findProductById(id: Int): Result<Product> {
        return runCatching {
            val product = productDao.findById(id)
            if (product != null) {
                product
            } else {
                throw Exception("Produto não encontrado")
            }
        }
    }

    override suspend fun createProduct(product: Product?): Result<Unit> {
        return runCatching {
            if (product != null) {
                productDao.createProduct(product)
            } else {
                throw Exception("Produto nulo não pode ser criado")
            }
        }.onFailure { e ->
            println(e)
        }
    }

    override suspend fun createProducts(products: List<Product>): Result<Unit> {
        return runCatching {
            if (products.isNotEmpty()) {
                productDao.createProducts(*products.toTypedArray())
            } else {
                throw Exception("Lista de produtos vazia não pode ser criada")
            }
        }.onFailure { e ->
            println(e)
        }
    }


    override suspend fun createCategory(category: Category?): Result<Unit> {
        return runCatching {
            if (category != null) {
                categoryDao.createCategory(category)
            } else {
                throw Exception("Categoria nula não pode ser criado")
            }
        }.onFailure { e ->
            println(e)
        }
    }

    override suspend fun createCategories(categories: List<Category>): Result<Unit> {
        return runCatching {
            if (categories.isNotEmpty()) {
                categoryDao.createCategories(*categories.toTypedArray())
            } else {
                throw Exception("Lista de categorias vazia não pode ser criada")
            }
        }.onFailure { e ->
            println(e)
        }
    }
}