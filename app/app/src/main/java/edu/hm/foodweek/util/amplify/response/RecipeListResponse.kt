package edu.hm.foodweek.util.amplify.response

import com.google.gson.annotations.SerializedName
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.recipes.persistence.model.Recipe

data class RecipeListResponse (
    @SerializedName("content")
    var recipes: List<Recipe>,
    @SerializedName("totalPages")
    var totalPages: Int,
    @SerializedName("totalElements")
    var totalAmountOfMealPlans: Int,
    @SerializedName("first")
    var firstPages: Boolean,
    @SerializedName("number")
    var pageNumber: Int,
    @SerializedName("numberOfElements")
    var numberOfElementsOnPage: Int,
    @SerializedName("last")
    var lastPage: Boolean
)