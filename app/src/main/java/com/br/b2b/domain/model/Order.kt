package com.br.b2b.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.br.b2b.data.local.converter.CartItemConverter
import javax.annotation.concurrent.Immutable

@Immutable
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    val userId: String,
    val requestedAt: String,
    val status: String,
    val total: String,
    val trackingNumber: String,
    @TypeConverters(CartItemConverter::class)
    val items: List<CartItem> = mutableListOf()
)
