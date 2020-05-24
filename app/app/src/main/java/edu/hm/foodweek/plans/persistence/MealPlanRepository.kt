package edu.hm.foodweek.plans.persistence

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.hm.foodweek.FoodWeekDatabase
import edu.hm.foodweek.plans.persistence.model.MealPlan


class MealPlanRepository(context: Context) {

    val dao = FoodWeekDatabase.getInstance(context).mealPlanDao()

    fun getAllMealPlans(): LiveData<List<MealPlan>> {
        return dao.getAllMealPlans()
    }

    fun getMealPlanbyId(id:Long) : LiveData<MealPlan>{
        return dao.getMealPlan(id)
    }

    suspend fun createMealPlan(mealPlan: MealPlan) {
         dao.createMealPlan(mealPlan)
    }

    suspend fun deleteMealPlan(mealPlan: MealPlan) {
        return dao.deleteMealPlan(mealPlan)
    }

    suspend fun updateMealPlan(mealPlan: MealPlan) {
        return dao.updateMealPlan(mealPlan)
    }

    companion object {
        private var instance: MealPlanRepository? = null

        @Synchronized
        private fun createInstance(context: Context) {
            if (instance == null) {
                instance =
                    MealPlanRepository(context)
            }
        }

        fun getInstance(context: Context): MealPlanRepository {
            if (instance == null) createInstance(context)
            return instance!!
        }
    }

}