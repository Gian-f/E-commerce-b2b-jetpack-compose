package com.br.b2b.domain.repository

import com.br.b2b.data.local.converter.convertProduct
import com.br.b2b.data.local.dao.CategoryDao
import com.br.b2b.data.local.dao.ProductDao
import com.br.b2b.data.remote.service.ApiService
import com.br.b2b.domain.model.Category
import com.br.b2b.domain.model.Product
import javax.inject.Inject

class StoreRepositoryImpl
@Inject constructor(
    private val service: ApiService,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
) : StoreRepository {

    override suspend fun fetchAllRecommendedProducts(): Result<List<Product>> {
        return runCatching {
            productDao.getAllRecommendedProducts()
        }
    }

    override suspend fun fetchAllProducts(): Result<List<Product>> {
        val localProducts = productDao.getAllProducts()
        if (localProducts.isNotEmpty()) {
            return Result.success(localProducts)
        }
        return runCatching {
            val response = service.fetchAllProducts()
            if (response.isSuccessful) {
                val products = convertProduct(response.body()?.result ?: emptyList())
                products
            } else {
                throw Exception("Erro ao trazer produtos: ${response.message()}")
            }
        }
    }

    override suspend fun fetchAllCategories(): Result<List<Category>> {
        val localCategories = categoryDao.getAllCategories()
        if (localCategories.isNotEmpty()) {
            return Result.success(localCategories)
        }

        return runCatching {
            val response = service.fetchAllCategories()
            if (response.isSuccessful) {
                response.body()?.result ?: emptyList()
            } else {
                throw Exception("Erro ao trazer categorias: ${response.message()}")
            }
        }
    }

    override suspend fun findProductById(id: Int): Result<Product?> {
        return runCatching {
            productDao.findById(id)
        }
    }

    override suspend fun findProducts(term: String): Result<List<Product?>> {
        return runCatching {
            productDao.findProducts("%${term}%")
        }
    }

    override suspend fun createProduct(product: Product): Result<Unit> {
        return runCatching {
            productDao.createProduct(product)
        }
    }

    override suspend fun createProducts(products: List<Product>): Result<Unit> {
        return runCatching {
            productDao.createProducts(*products.toTypedArray())
        }
    }

    override suspend fun createCategory(category: Category): Result<Unit> {
        return runCatching {
            categoryDao.createCategory(category)
        }
    }

    override suspend fun createCategories(categories: List<Category>): Result<Unit> {
        return runCatching {
            categoryDao.createCategories(*categories.toTypedArray())
        }
    }

    override suspend fun getProductsInCategory(categoryId: Int): Result<List<Product>> {
        return runCatching {
            productDao.getProductsByCategory(categoryId)
        }
    }

    override suspend fun toggleFavoriteStatus(productId: Int): Result<Unit> {
        return runCatching {
            val product = productDao.findById(productId)
            productDao.updateFavoriteStatus(productId, !product.isFavorited)
        }
    }
}