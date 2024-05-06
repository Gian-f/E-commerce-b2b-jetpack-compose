package com.br.b2b.util

sealed class Section {
    data object Categories : Section() {
        const val id = "categories"
        const val contentType = "categories"
    }

    data object Banners : Section() {
        const val id = "banners"
        const val contentType = "banners"
    }

    data object FeaturedProducts : Section() {
        const val id = "featuredProducts"
        const val contentType = "featuredProducts"
    }

    data object FilteredProducts : Section() {
        const val id = "filteredProducts"
        const val contentType = "filteredProducts"
    }

    data object RecommendedProducts : Section() {
        const val id = "recommendedProducts"
        const val contentType = "recommendedProducts"
    }
}