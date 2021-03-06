package edu.hm.foodweek.util.amplify.response

import com.google.gson.annotations.SerializedName
import edu.hm.foodweek.plans.persistence.model.MealPlan

data class MealPlanResponse (
    @SerializedName("content")
    var mealPlans: List<MealPlan>,
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