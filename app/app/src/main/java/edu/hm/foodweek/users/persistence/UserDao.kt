package edu.hm.foodweek.users.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.hm.foodweek.users.persistence.model.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Update
    suspend fun update(user: User)

    // acutally there is only one User per app
    @Query(value = "SELECT * FROM users Limit 1")
    fun getLiveDataUser(): LiveData<User>

    @Transaction
    @Query(value = "SELECT * FROM users Limit 1")
    suspend fun getUser(): User


    @Delete
    fun delete(user: User)


}