package edu.hm.foodweek.recipes

data class Recipe(
    val title: String,
    val description: String,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val labels: Set<String>
)