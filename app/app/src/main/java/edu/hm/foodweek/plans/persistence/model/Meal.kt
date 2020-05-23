package edu.hm.foodweek.plans.persistence.model

import androidx.room.*
import edu.hm.foodweek.recipes.persistence.model.Recipe

@Entity
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val mealId: Long,
    val mealPlanId: Long,
    val day: WeekDay,
    val time: MealTime
)

data class MealAndRecipe(
    @Embedded
    val meal: Meal,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "recipeId",
        associateBy = Junction(MealRecipeCrossRef::class)
    )
    val recipe: Recipe
)

@Entity(primaryKeys = ["mealId", "recipeId"], indices = [Index("recipeId")])
data class MealRecipeCrossRef(
    val mealId: Long,
    val recipeId: Long
)