package edu.hm.foodweek.util.amplify

import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.util.amplify.response.MealPlanResponse
import edu.hm.foodweek.util.amplify.response.RecipeListResponse
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

    @GET("/recipes")
    fun getRecipes(@Query("page") page: Int, @Query("size") size: Int, @Query("search") searchQuery: String?): Call<RecipeListResponse>

    @POST("/mealplans")
    fun publishMealPlan(@Body mealPlan: MealPlan, @Header("user") userId: String): Call<MealPlan>

    @PUT("/mealplans/{mealPlanId}")
    fun updateMealPlan(@Path("mealPlanId") mealPlanId: Long, @Body mealPlan: MealPlan, @Header("user") userId: String): Call<MealPlan>


}