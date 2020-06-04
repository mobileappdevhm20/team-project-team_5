package edu.hm.foodweek.plans.screen.details

import edu.hm.foodweek.plans.persistence.model.WeekDay

data class PlanDetailsItem(
    val day: WeekDay,
    val dishImageURL: String,
    val breakfastTitle: String,
    val lunchTitle: String,
    val dinnerTitle: String
)