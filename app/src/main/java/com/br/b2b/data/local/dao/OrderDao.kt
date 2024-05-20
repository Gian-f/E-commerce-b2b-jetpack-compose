package com.br.b2b.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.br.b2b.domain.model.Order

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders")
    fun getAllOrders(): List<Order>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    fun findById(id: Long): Order?

    @Query("DELETE FROM orders")
    fun deleteAll()

    @Insert
    fun insertAll(vararg orders: Order)

    @Update
    fun update(order: Order)
}