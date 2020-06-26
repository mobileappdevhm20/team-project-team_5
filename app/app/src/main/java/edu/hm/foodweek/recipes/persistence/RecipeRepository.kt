package edu.hm.foodweek.recipes.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.util.amplify.FoodWeekClient
import edu.hm.foodweek.util.amplify.response.RecipeListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class RecipeRepository(private val dao: RecipeDao, private val foodWeekClient: FoodWeekClient) {
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes
    fun getAllRecipes(page: Int = 0, size: Int = 20, query: String? = null): LiveData<List<Recipe>> {
        val realQuery = if (query?.trim().isNullOrEmpty()) {
            null
        } else {
            query?.trim()
        }
        if (page == 0) {
            _recipes.postValue(emptyList())
        }
        val backendCall = foodWeekClient
            .getFoodWeekServiceClient()
            .getRecipes(page, size, realQuery)
        backendCall.enqueue(AllRecipesCallBack(_recipes))
        return recipes
    }

    fun getLiveDataRecipesByIds(ids: List<Long>): LiveData<List<Recipe>> {
        return dao.getRecipes(ids)
    }

    fun getLiveDataRecipeById(id: Long): LiveData<Recipe> {
        val recipeFromRoom = dao.getRecipe(id)
        if (recipeFromRoom.value != null) {
            return recipeFromRoom
        }

        val liveDataRecipe = MutableLiveData<Recipe>()
        foodWeekClient.getFoodWeekServiceClient().getRecipe(id).enqueue(object : Callback<Recipe> {
            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                Log.println(
                    Log.ERROR,
                    "RecipeRepository",
                    "HTTP-Request /recipes/$id failed: ${t.message}"
                )
            }

            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                if (response.isSuccessful) {
                    Log.println(Log.INFO, "RecipeRepository", "HTTP-Request /recipes/$id was successful: ${response.code()}")
                    liveDataRecipe.postValue(response.body())
                } else {
                    Log.println(Log.INFO, "RecipeRepository", "HTTP-Request /recipes/$id was not successful: ${response.code()}")
                }
            }

        })
        return liveDataRecipe
    }

    fun getRecipeById(id: Long): Recipe {
        val result = foodWeekClient.getFoodWeekServiceClient().getRecipe(id).execute()
        if (!result.isSuccessful) {
            Log.println(Log.ERROR, "RecipeRepository", "Failed retrieving Recipe from Backend Database")
        }
        return result.body()!!
    }

    class AllRecipesCallBack(val list: MutableLiveData<List<Recipe>>) : Callback<RecipeListResponse> {
        override fun onFailure(call: Call<RecipeListResponse>, t: Throwable) {
            Log.println(
                Log.ERROR,
                "RecipeRepository",
                "HTTP-Request /recipes failed: ${t.message}"
            )
        }

        override fun onResponse(call: Call<RecipeListResponse>, response: Response<RecipeListResponse>) {
            Log.println(Log.INFO, "RecipeRepository", "HTTP-Request /recipes was successful: ${response.code()}")
            val foundMealPlans = response.body()?.recipes ?: emptyList()
            val acutalList = list.value ?: emptyList()
            list.postValue(acutalList.plus(foundMealPlans))
        }
    }
}