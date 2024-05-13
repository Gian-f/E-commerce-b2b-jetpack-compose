package com.br.b2b.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.br.b2b.domain.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    fun findByEmail(email: String): User?


    @Query("DELETE FROM user")
    fun deleteAll()

    @Insert
    fun insertAll(vararg users: User)

    @Update
    fun update(user: User)
}