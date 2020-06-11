package edu.hm.foodweek.util.amplify

import com.google.gson.annotations.SerializedName
import edu.hm.foodweek.plans.persistence.model.MealPlan

data class MealPlanContent (
    @SerializedName("content")
    var mealPlans: List<MealPlan>
)