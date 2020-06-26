package edu.hm.foodweek.users.persistence


import androidx.lifecycle.LiveData
import edu.hm.foodweek.users.persistence.model.User
import edu.hm.foodweek.util.extensions.mapSkipNulls
import kotlinx.coroutines.runBlocking
import java.util.*

open class UserRepository(private val userDao: UserDao) {
    fun getUser(): LiveData<User> {
        return userDao.getLiveDataUser()
    }

    suspend fun getUserNoLiveData(): User {
        return userDao.getUser()
    }


    fun getCurrentWeek(): LiveData<Long> {
        val user = userDao.getLiveDataUser()
        return user.mapSkipNulls {
            var currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
            return@mapSkipNulls it.weekMealPlanMap.get(currentWeek) ?: -1
        }
    }

    suspend fun setMealToWeek(mealId: Long, week: Int) {
        runBlocking {
            val user = getUserNoLiveData()
            if (user == null) {
                return@runBlocking
            }
            val userMap = user.weekMealPlanMap as HashMap
            userMap[week] = mealId
            user.weekMealPlanMap = userMap
            userDao.update(user)
        }
    }


}