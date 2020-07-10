package edu.hm.foodweek.recipes.screen.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.util.extensions.mapSkipNulls


class RecipeDetailViewModel(
    recipeId: Long,
    recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {


    private val recipe = recipeRepository.getLiveDataRecipeById(recipeId)

    val title = recipe.mapSkipNulls {
        it.title
    }
    val url = recipe.mapSkipNulls { it.url }
    val ingredients = recipe.mapSkipNulls { it.ingredients }

    val steps = recipe.mapSkipNulls { it.steps }

}