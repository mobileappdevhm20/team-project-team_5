package edu.hm.foodweek.recipes.persistence.model

import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("name")
    val name: String
)