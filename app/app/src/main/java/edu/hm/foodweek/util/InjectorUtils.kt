package edu.hm.foodweek.util

import android.content.Context
import edu.hm.foodweek.plans.MealPlanRepository
import edu.hm.foodweek.recipes.RecipeRepository

object InjectorUtils {

    private fun getRecipeRepository(context: Context): RecipeRepository {
        return RecipeRepository.getInstance(context)
    }

    private fun getMealPlanRepository(context: Context): MealPlanRepository {
        return MealPlanRepository.getInstance(context)
    }
}