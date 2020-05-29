package edu.hm.foodweek.recipes

import android.app.Application
import androidx.lifecycle.*
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import kotlinx.coroutines.launch


class RecipeDetailViewModel(
    private val recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {
    val recipeId = MutableLiveData<Long>()


    private val recipe = recipeId.switchMap { id ->
        liveData {
            emit(recipeRepository.getRecipeById(id))
        }
    }

    val title = recipe.map { it.title }

    val ingredients = recipe.map { it.ingredients }

    val steps = recipe.map { it.steps }

}