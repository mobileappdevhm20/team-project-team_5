package edu.hm.foodweek.plans

import edu.hm.foodweek.recipes.Ingredient
import edu.hm.foodweek.recipes.Recipe

class MealPlan(
    val title:String
) {
    val draft = true
    private val week = Array(7, { RecipesPerDay() })

    fun getWeek(): Array<RecipesPerDay> {
        return week.copyOf()
    }

    fun setDaysRecipe(day: Int, recipesPerDay: RecipesPerDay) {
        if (day >= 0 && day < week.size)
            week[day] = recipesPerDay
    }

    data class RecipesPerDay(
        val breakfast: List<Recipe> = emptyList(),
        val lunch: List<Recipe> = emptyList(),
        val dinner: List<Recipe> = emptyList()
    )

    fun getShoppingList(): List<Ingredient> {
        return week
            .map { listOf(it.breakfast, it.lunch, it.dinner).flatten() }
            .flatten()
            .map { it.ingredients }
            .flatten()
    }

}