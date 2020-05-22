package edu.hm.foodweek.plans.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import edu.hm.foodweek.plans.persistence.model.MealAndRecipe
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealPlanWithMealsAndRecipes

@Dao
interface MealPlanDao {

    @Transaction
    @Query("SELECT * FROM MealPlan")
    fun getAllMealPlans(): LiveData<List<MealPlan>>

    @Transaction
    @Query("SELECT * FROM MealPlan WHERE PlanId = :planId")
    fun getPlanWithMealsAndRecipes(planId: Long): LiveData<MealPlanWithMealsAndRecipes>

    @Transaction
    @Query("SELECT * FROM Meal")
    fun getMealAndRecipe(): LiveData<List<MealAndRecipe>>

}
