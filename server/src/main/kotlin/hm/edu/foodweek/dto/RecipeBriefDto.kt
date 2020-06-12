package hm.edu.foodweek.dto

data class RecipeBriefDto(
        var recipeId: Long? = null,
        var title: String? = null,
        var description: String? = null,
        var image: String? = null
)