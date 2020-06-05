package edu.hm.foodweek.plans.persistence.model

data class Meal(
    val day: WeekDay,
    val time: MealTime,
    // this recipe ID is not checked with the database
    val recipeId: Long
)