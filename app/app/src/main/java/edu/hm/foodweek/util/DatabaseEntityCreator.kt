package edu.hm.foodweek.util

import android.util.Log
import edu.hm.foodweek.plans.persistence.MealPlanDao
import edu.hm.foodweek.plans.persistence.RecipeDao
import edu.hm.foodweek.plans.persistence.model.Meal
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.model.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseEntityCreator {
    // Insert data using daos
    val recipe1 = Recipe(
        0,
        "Initial Recipe",
        "Initial description"
    )

    val mealplan1 = MealPlan(
        0,
        "Inital Title",
        "Inital description",
        "UTL",
        1,
        true,
        listOf(recipe1).map { recipe -> Meal(WeekDay.MONDAY, MealTime.BREAKFAST, recipe.recipeId) }
    )

    fun createRecipes(): List<Recipe> {
        return listOf(recipe1)
    }


    fun createMealPlans(): List<MealPlan> {
        return listOf(mealplan1)
    }

    fun insertPresetIntoDatabase(recipeDao: RecipeDao, mealPlanDao: MealPlanDao) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("FoodWeekDatabase", "Insert recipe")
            for (recipe in createRecipes()) {
                recipeDao.createRecipe(recipe)
                Log.i("DatabaseEntityCreator", "recipe ${recipe.recipeId} created")
            }
            for (mealPlan in createMealPlans()) {
                mealPlanDao.createMealPlan(mealPlan)
                Log.i("DatabaseEntityCreator", "mealplan ${mealPlan.planId} created")
            }
        }.invokeOnCompletion {
            Log.i("DatabaseEntityCreator", "database insertion finished")
        }
    }


}