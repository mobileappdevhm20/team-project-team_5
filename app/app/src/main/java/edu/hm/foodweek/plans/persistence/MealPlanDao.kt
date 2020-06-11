package edu.hm.foodweek.plans.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.hm.foodweek.plans.persistence.model.MealPlan

@Dao
interface MealPlanDao {

    @Transaction
    @Query("SELECT * FROM MealPlan")
    fun getAllMealPlans(): LiveData<List<MealPlan>>

    @Transaction
    @Query("SELECT * FROM MealPlan where planId = :id ")
    fun getMealPlan(id: Long): LiveData<MealPlan>

    @Transaction
    @Query("SELECT * FROM MealPlan where planId = :id ")
    fun getMealPlanNoLiveData(id: Long): MealPlan

    @Transaction
    @Query("SELECT * FROM MealPlan where creatorId = :id ")
    fun getMealPlanCreatedByUser(id: String): LiveData<List<MealPlan>>

    @Transaction
    @Query("SELECT * FROM MealPlan where creatorId = :id ")
    fun getMealPlanCreatedByUserNoLiveData(id: Long): List<MealPlan>

    @Insert
    suspend fun createMealPlan(mealPlan: MealPlan): Long

    @Update
    suspend fun updateMealPlan(mealPlan: MealPlan)

    @Delete
    suspend fun deleteMealPlan(mealPlan: MealPlan)
}
