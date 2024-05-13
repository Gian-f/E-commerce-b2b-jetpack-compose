package com.br.b2b.domain.repository

import com.br.b2b.data.local.dao.UserDao
import com.br.b2b.domain.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {
    override fun getAllUsers(): List<User> {
            return userDao.getAllUsers()
    }

    override fun findByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    override fun insertAll(vararg users: User) {
        runCatching {
            userDao.insertAll(*users)
        }.onFailure {
            println(it.message)
        }
    }

    override fun update(user: User) {
        userDao.update(user)
    }

    override fun deleteAll() {
        userDao.deleteAll()
    }
}