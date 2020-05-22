package edu.hm.foodweek.plans.persistence.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class MealPlan(
    @PrimaryKey(autoGenerate = true)
    val planId: Long,
    val title: String,
    val creatorId: Long,
    var draft: Boolean = true
)

data class MealPlanWithMealsAndRecipes(
    @Embedded
    val plan: MealPlan,
    @Relation(
        entity = Meal::class,
        parentColumn = "planId",
        entityColumn = "mealPlanId"
    )
    var meals: List<MealAndRecipe>
)