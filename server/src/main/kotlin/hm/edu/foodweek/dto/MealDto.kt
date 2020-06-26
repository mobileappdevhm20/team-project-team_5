package hm.edu.foodweek.dto

import hm.edu.foodweek.model.MealTime
import hm.edu.foodweek.model.WeekDay

data class MealDto(
        var mealId: Long? = null,
        var day: WeekDay? = null,
        var time: MealTime? = null,
        var recipe: RecipeBriefDto? = null
)