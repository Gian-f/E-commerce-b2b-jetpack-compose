package com.br.b2b.domain.model

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Immutable
@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val name: String,
    val status: Boolean,
    val cpf: String,
    val termsConditions: String,
    val enabled: Boolean,
    val password: String? = null,
    val createdAt: String,
    val updatedAt: String
)