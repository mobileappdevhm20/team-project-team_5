package hm.edu.foodweek.dto

data class MealPlanBriefDto(
        var planId: Long? = null,
        var title: String? = null,
        var description: String? = null,
        var imageURL: String? = null,
        var draft: Boolean? = null
)