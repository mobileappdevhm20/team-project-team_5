package edu.hm.foodweek.plans.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class MealPlan(
    @SerializedName("planId")
    @PrimaryKey(autoGenerate = true)
    var planId: Long = 0,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("imageURL")
    var imageURL: String = "",
    @Expose(serialize = false, deserialize = false)
    var creatorId: String = "",
    @SerializedName("draft")
    var draft: Boolean = true,
    @SerializedName("creatorUsername")
    val creatorUsername: String = "",
    @SerializedName("meals")
    var meals: List<Meal> = emptyList()
)