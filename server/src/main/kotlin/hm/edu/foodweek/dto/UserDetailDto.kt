package hm.edu.foodweek.dto

data class UserDetailDto(
        var userId: String = "",
        var username: String? = "",
        var ownMealPlans: List<MealPlanBriefDto>? = emptyList(),
        var subscribedPlans: List<MealPlanBriefDto>? = emptyList()
)