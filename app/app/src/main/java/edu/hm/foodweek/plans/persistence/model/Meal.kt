package edu.hm.foodweek.plans.persistence.model

import androidx.room.*
import edu.hm.foodweek.recipes.persistence.model.Recipe

data class Meal(
    val day: WeekDay,
    val time: MealTime,
    val recipeId: String
)