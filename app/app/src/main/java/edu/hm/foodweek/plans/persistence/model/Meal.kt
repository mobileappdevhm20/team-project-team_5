package edu.hm.foodweek.plans.persistence.model

import edu.hm.foodweek.recipes.persistence.model.Recipe

data class Meal(
    val day: WeekDay,
    val time: MealTime,
    val recipe: Recipe
)
