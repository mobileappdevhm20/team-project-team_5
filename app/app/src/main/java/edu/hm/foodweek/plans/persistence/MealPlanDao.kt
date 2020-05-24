package edu.hm.foodweek.plans.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.hm.foodweek.plans.persistence.model.MealPlan

@Dao
interface MealPlanDao {

    @Transaction
    @Query("SELECT * FROM MealPlan")
    fun getAllMealPlans(): LiveData<List<MealPlan>>

    @Insert
    suspend fun createMealPlan(mealPlan: MealPlan): Long

    @Update
    suspend fun updateMealPlan(mealPlan: MealPlan)

    @Delete
    suspend fun deleteMealPlan(mealPlan: MealPlan)
}
