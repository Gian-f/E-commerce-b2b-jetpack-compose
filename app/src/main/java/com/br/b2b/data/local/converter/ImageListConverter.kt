package com.br.b2b.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImageListConverter {

    @TypeConverter
    fun fromList(images: List<String>): String {
        val gson = Gson()
        return gson.toJson(images)
    }

    @TypeConverter
    fun toList(imagesJson: String): List<String> {
        val gson = Gson()
        val typeToken = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(imagesJson, typeToken)
    }
}
