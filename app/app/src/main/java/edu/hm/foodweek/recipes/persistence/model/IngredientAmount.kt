package edu.hm.foodweek.recipes.persistence.model

import com.google.gson.annotations.SerializedName

data class IngredientAmount(
    @SerializedName("ingredient")
    val ingredient: Ingredient,
    @SerializedName("measure")
    val measure: String
)