package edu.hm.foodweek.plans

import androidx.room.*
import edu.hm.foodweek.recipes.Ingredient
import edu.hm.foodweek.recipes.Recipe
import edu.hm.foodweek.storage.User

@Entity(tableName = "meal_plan")
data class MealPlan(
    @PrimaryKey
    val title: String,
    @Embedded
    val creator: User
) {
    @Ignore
    val draft = true
    @Ignore
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