package edu.hm.foodweek.util.amplify.response

import edu.hm.foodweek.recipes.persistence.model.IngredientAmount
import edu.hm.foodweek.recipes.persistence.model.Recipe

data class RecipeResponse(
    val recipeId: Long,
    val title: String,
    val description: String,
    val url: String? = "",
    val ingredients: List<IngredientAmount> = emptyList(),
    val steps: List<String> = emptyList(),
    val labels: Set<String> = emptySet()
) {
    fun asRecipe(): Recipe {
        return Recipe(
            recipeId = recipeId,
            title = title,
            description = description,
            url = url ?: "",
            steps = steps,
            labels = labels,
            ingredients = ingredients
        )
    }
}