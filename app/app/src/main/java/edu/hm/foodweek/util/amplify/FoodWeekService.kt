package edu.hm.foodweek.util.amplify

import edu.hm.foodweek.plans.persistence.model.MealPlan
import retrofit2.Call
import retrofit2.http.GET

interface FoodWeekService {

    @GET("/mealplans")
    fun getMealPlans(): Call<List<MealPlan>>
}