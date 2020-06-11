package edu.hm.foodweek.plans.persistence.model

import androidx.room.*
import edu.hm.foodweek.users.persistence.model.User

@Entity
data class MealPlan(
    @PrimaryKey(autoGenerate = true)
    val planId: Long,
    val title: String,
    val description: String,
    val imageURL: String,
    val creatorId: String,
    var draft: Boolean = true,
    var meals: List<Meal> = emptyList()
)

