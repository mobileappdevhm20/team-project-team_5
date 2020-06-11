package edu.hm.foodweek.util.amplify

import retrofit2.Call
import retrofit2.http.GET

interface FoodWeekService {

    @GET("/mealplans")
    fun getMealPlans(): Call<MealPlanContent>
}