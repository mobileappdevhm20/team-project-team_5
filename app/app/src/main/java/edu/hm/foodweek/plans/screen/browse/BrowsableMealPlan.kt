package edu.hm.foodweek.plans.screen.browse

import edu.hm.foodweek.plans.persistence.model.MealPlan

data class BrowsableMealPlan(
    val plan: MealPlan,
    var subscribed: Boolean = false,
    var owned: Boolean = false
)