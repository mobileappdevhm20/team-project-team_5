package edu.hm.foodweek.plans.persistence.model

import edu.hm.foodweek.recipes.persistence.model.Recipe

data class Meal(
    val day: WeekDay,
    val time: MealTime,
    // this recipe ID is not checked with the database
    val recipe: Recipe
)