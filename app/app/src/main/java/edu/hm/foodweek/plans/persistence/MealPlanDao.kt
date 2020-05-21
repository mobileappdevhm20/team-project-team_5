package edu.hm.foodweek.plans.persistence

import androidx.room.*
import edu.hm.foodweek.plans.MealPlan

@Dao
interface MealPlanDao {
    @Insert
    fun insert(mealPlan: MealPlan)

    @Update
    fun update(mealPlan: MealPlan)

    @Query(value = "SELECT * FROM meal_plan WHERE title = :title")
    fun get(title: String): MealPlan?

    @Delete
    fun delete(mealPlan: MealPlan)
}