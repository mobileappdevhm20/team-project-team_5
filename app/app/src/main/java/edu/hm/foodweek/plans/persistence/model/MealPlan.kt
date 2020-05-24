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
    val creatorId: Long,
    var draft: Boolean = true,
    // these ids aren't verfiyed
    var meals : List<Meal> = emptyList()
)

