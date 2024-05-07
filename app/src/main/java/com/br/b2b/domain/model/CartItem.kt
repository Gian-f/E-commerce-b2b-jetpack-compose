package com.br.b2b.domain.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["productId"])]
)
@Immutable
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "productId") val productId: Int,
    val quantity: Int = 1,
    val unitPrice: Double,
    val totalPrice: Double,
    val productImages: List<String>,
    val productTitle: String,
    val productCategoryId: Int,
    val status: String = "Disponível",
    val productDetails: String, // Detalhes do produto
    val promotions: String, // Informações sobre promoções/descontos
    val shippingFees: Double, // Taxas de envio
    val currentStock: Int = 10,
    val estimatedDeliveryDate: String, // Data de entrega estimada
    val productReviews: String, // Links para avaliações de produtos
    val warrantyInfo: String, // Informações sobre garantia
    val returnPolicy: String // Informações sobre política de devolução
)

