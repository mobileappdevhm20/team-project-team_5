package edu.hm.foodweek.recipes.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Long,
    val title: String,
    val description: String,
    val url: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<String> = emptyList(),
    val labels: Set<String> = emptySet()
)