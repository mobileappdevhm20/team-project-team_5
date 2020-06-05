package edu.hm.foodweek.plans.screen.details

import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.model.Recipe

data class PlanDayDetailsItem(
    val day: WeekDay,
    val time: MealTime,
    val recipes: List<Recipe>
)