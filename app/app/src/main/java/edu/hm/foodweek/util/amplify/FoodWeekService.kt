package edu.hm.foodweek.util.amplify

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodWeekService {

    @GET("/mealplans")
    fun getMealPlans(@Query("page") page: Int, @Query("size") size: Int, @Query("search") searchQuery: String?): Call<MealPlanResponse>
}