package edu.hm.foodweek.users.persistence

import androidx.room.*
import edu.hm.foodweek.users.persistence.model.User

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