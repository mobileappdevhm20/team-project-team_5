package edu.hm.foodweek.week

import edu.hm.foodweek.plans.persistence.model.MealTime

data class MealPreview(
    val mealTime:MealTime,
    val recipeName:String,
    val recipeId:Long,
    val url:String
) {
}