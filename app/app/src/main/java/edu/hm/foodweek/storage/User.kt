package edu.hm.foodweek.storage

import edu.hm.foodweek.plans.MealPlan

class User {

    val createdMealPlans: MutableList<String> = mutableListOf()

    fun addMealPlan(mealPlan: MealPlan) {
        createdMealPlans.add(mealPlan.title)
    }

}