

package edu.hm.foodweek.recipes.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Recipe(
    @SerializedName("recipeId")
    @PrimaryKey(autoGenerate = true)
    val recipeId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val url: String = "",
    @SerializedName("ingredients")
    val ingredients: List<IngredientAmount> = emptyList(),
    @SerializedName("steps")
    val steps: List<String> = emptyList(),
    @SerializedName("labels")
    val labels: Set<String> = emptySet()
)

