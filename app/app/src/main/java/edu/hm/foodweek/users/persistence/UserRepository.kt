package edu.hm.foodweek.users.persistence


import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.users.persistence.model.User
import edu.hm.foodweek.util.extensions.mapSkipNulls
import java.util.*
import java.util.logging.Logger
import kotlin.collections.HashMap

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

    suspend fun setMealToCurrentWeek(mealPlan: MealPlan) {
        var currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
        setMealToWeek(mealPlan, currentWeek)
    }

    suspend fun setMealToWeek(mealPlan: MealPlan, week: Int) {
        val user = getUser().value
        if (user == null) {
            return
        }
        val userMap = user.weekMealPlanMap as HashMap
        userMap.put(week, mealPlan.planId)
        user.weekMealPlanMap = userMap
        userDao.update(user)
    }

}