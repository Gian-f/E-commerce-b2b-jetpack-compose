package com.br.b2b.data.local.converter

import androidx.room.TypeConverter
import com.br.b2b.domain.model.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartItemConverter {
    private val gson = Gson()

    @TypeConverter
    fun cartItemListToJson(cartItemList: List<CartItem>): String {
        return gson.toJson(cartItemList)
    }

    @TypeConverter
    fun jsonToCartItemList(json: String): List<CartItem> {
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type)
    }
}