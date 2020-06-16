package edu.hm.foodweek.util.amplify

import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.recipes.persistence.model.Recipe
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface FoodWeekService {

    @GET("/mealplans")
    fun getMealPlans(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("search") searchQuery: String?
    ): Call<MealPlanResponse>

    @GET("/users/{userId}/subscribed")
    fun getSubscribedMealplans(
        @Path("userId") userId: String,
        @Header("user") user: String
    ): Observable<List<MealPlan>>

    @GET("/users/{userId}/owned")
    fun getOwnedMealplans(
        @Path("userId") userId: String,
        @Header("user") user: String
    ): Observable<List<MealPlan>>

    @PUT("/users/{userId}/subscribed/{mealPlanId}")
    fun subscribeMealPlan(
        @Path("userId") userId: String,
        @Path("mealPlanId") mealPlanId: Long,
        @Header("user") user: String
    ): Observable<MealPlan>

    @DELETE("/users/{userId}/subscribed/{mealPlanId}")
    fun unsubscribeMealPlan(
        @Path("userId") userId: String,
        @Path("mealPlanId") mealPlanId: Long,
        @Header("user") user: String
    ): Observable<MealPlan>

    @GET("/mealplans/{mealPlanId}")
    fun getMealPlan(@Path("mealPlanId") mealPlanId: Long): Call<MealPlan>

    @GET("/recipes/{recipeId}")
    fun getRecipe(@Path("recipeId") recipeId: Long): Call<Recipe>

    @DELETE("/mealplans/{mealPlanId}")
    fun deleteMealPlan(
        @Path("mealPlanId") mealPlanId: Long,
        @Header("user") userId: String
    ): Observable<Unit>
}