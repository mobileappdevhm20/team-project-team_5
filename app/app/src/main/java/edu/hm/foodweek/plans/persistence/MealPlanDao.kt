package edu.hm.foodweek.plans.persistence

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import edu.hm.foodweek.plans.MealPlanWithMealsAndRecipes

@Dao
interface MealPlanDao {

    @Transaction
    @Query("SELECT * FROM MealPlan")
    fun getPlansWithMealsAndRecipes(): List<MealPlanWithMealsAndRecipes>

}
