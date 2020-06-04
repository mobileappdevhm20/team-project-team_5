package edu.hm.foodweek.plans.persistence

import androidx.lifecycle.LiveData
import edu.hm.foodweek.plans.persistence.model.MealPlan


open class MealPlanRepository(private val dao: MealPlanDao) {

    fun getLiveDataAllMealPlans(): LiveData<List<MealPlan>> {
        return dao.getAllMealPlans()
    }

    fun getLiveDataMealPlanById(id: Long): LiveData<MealPlan> {
        return dao.getMealPlan(id)
    }

    fun getMealPlanCreatedByUser(userId: Long): LiveData<List<MealPlan>> {
        return dao.getMealPlanCreatedByUser(userId)
    }

    suspend fun createMealPlan(mealPlan: MealPlan) {
        dao.createMealPlan(mealPlan)
    }
}