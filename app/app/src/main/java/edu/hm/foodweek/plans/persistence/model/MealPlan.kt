package edu.hm.foodweek.plans.persistence.model

import androidx.room.*
import edu.hm.foodweek.users.persistence.model.User

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["creatorId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MealPlan(
    @PrimaryKey(autoGenerate = true)
    val planId: Long,
    val title: String,
    val description: String,
    val imageURL: String,
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