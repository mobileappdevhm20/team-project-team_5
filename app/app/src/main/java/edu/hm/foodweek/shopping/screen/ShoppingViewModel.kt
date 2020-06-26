package edu.hm.foodweek.shopping.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.persistence.model.Ingredient
import edu.hm.foodweek.recipes.persistence.model.IngredientAmount
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.users.persistence.UserRepository
import edu.hm.foodweek.util.extensions.mapSkipNulls
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShoppingViewModel(
    private val userRepository: UserRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    val ingredients: LiveData<List<IngredientAmount>?> = userRepository.getCurrentWeek()
        .switchMap { mealPlanRepository.getLiveDataMealPlanById(it) }
        .switchMap { mealPlan -> liveData { emit(loadRecipes(mealPlan)) } }
        .mapSkipNulls { recipes ->
            recipes.map { it.ingredients }.flatten()
                .groupBy { ingredientAmount: IngredientAmount -> ingredientAmount.ingredient.name }
                .entries.map { entry ->
                    IngredientAmount(
                        ingredient = Ingredient(entry.key),
                        measure = entry.value.map { ingredientAmount -> ingredientAmount.measure }
                            .joinToString { it })
                }
        }

    private suspend fun loadRecipes(plan: MealPlan?): List<Recipe> =
        withContext(Dispatchers.IO) {
            return@withContext plan?.meals?.map { meal ->
                recipeRepository.getRecipeById(meal.recipe.recipeId)
            }
                ?: emptyList()
        }
}