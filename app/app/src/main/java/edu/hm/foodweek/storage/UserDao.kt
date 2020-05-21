package edu.hm.foodweek.storage

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query(value = "SELECT * FROM users WHERE email = :email")
    fun get(email: String): User?

    @Delete
    fun delete(user: User)
}