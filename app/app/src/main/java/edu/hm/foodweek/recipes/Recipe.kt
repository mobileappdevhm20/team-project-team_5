package edu.hm.foodweek.recipes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Long,
    val title: String,
    val description: String
    //val ingredients: List<Ingredient>,
    //val steps: List<String>,
    //val labels: Set<String>
)