package edu.hm.foodweek.plans.persistence

import androidx.lifecycle.LiveData
import edu.hm.foodweek.plans.persistence.model.MealPlan


open class MealPlanRepository(private val dao: MealPlanDao) {

    fun getLiveDataAllMealPlans(): LiveData<List<MealPlan>> {
        return dao.getAllMealPlans()
    }

    fun getLiveDataMealPlanById(mealplanId: Long): LiveData<MealPlan> {
        val mealplanlivedata = dao.getMealPlan(mealplanId)
        return mealplanlivedata
    }

    fun getMealPlanById(mealplanId: Long): MealPlan {
        return dao.getMealPlanNoLiveData(mealplanId)
    }

    fun getMealPlanCreatedByUser(userId: String): LiveData<List<MealPlan>> {
        return dao.getMealPlanCreatedByUser(userId)
    }

    fun getMealPlanCreatedByUserNoLiveData(userId: Long): List<MealPlan> {
        return dao.getMealPlanCreatedByUserNoLiveData(userId)
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

}