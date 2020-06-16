package edu.hm.foodweek.plans.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class MealPlan(
    @SerializedName("planId")
    @PrimaryKey(autoGenerate = true)
    val planId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("imageURL")
    val imageURL: String,
    @Expose(serialize = false, deserialize = false)
    val creatorId: String,
    @SerializedName("draft")
    var draft: Boolean = true,
    @SerializedName("creatorUsername")
    val creatorUsername: String,
    @SerializedName("meals")
    var meals: List<Meal> = emptyList()
)