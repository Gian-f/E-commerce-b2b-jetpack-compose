package com.br.b2b.domain.repository

import com.br.b2b.domain.model.User

interface UserRepository {
    fun getAllUsers(): List<User>
    fun findByEmail(email: String): User?
    fun insertAll(vararg users: User)
    fun update(user: User)
    fun deleteAll()
}