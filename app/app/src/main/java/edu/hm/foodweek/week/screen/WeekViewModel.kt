package edu.hm.foodweek.week.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.persistence.model.Recipe
import kotlinx.coroutines.launch

class WeekViewModel(
    private val mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository,
    application: Application
) :
    AndroidViewModel(
        application
    ) {
    val allMealPlans = mealPlanRepository.getLiveDataAllMealPlans()
    val allRecipes = recipeRepository.getAllRecipes()




    fun createRecipe() {
        viewModelScope.launch {
            recipeRepository.createRecipe(
                Recipe(
                    0,
                    "title 1",
                    "description",
                    emptyList(),
                    emptyList(),
                    emptySet()
                )
            )
        }
    }

}