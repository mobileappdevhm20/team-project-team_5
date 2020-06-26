package hm.edu.foodweek.dto

data class MealPlanDetailedDto(
        var planId: Long? = null,
        var title: String? = null,
        var description: String? = null,
        var imageURL: String? = null,
        var draft: Boolean? = null,
        var creatorUsername: String? = null,
        var meals: List<MealDto>? = emptyList()
)