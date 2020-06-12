package hm.edu.foodweek.dto

data class RecipeDetailedDto(
        var recipeId: Long? = null,
        var title: String? = null,
        var description: String? = null,
        var image: String? = null,
        var ingredients: List<IngredientAmountDto>? = emptyList(),
        var steps: List<String>? = emptyList(),
        var labels: Set<String>? = emptySet()
)