package edu.hm.foodweek.recipes.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.util.amplify.FoodWeekClient
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

open class RecipeRepository(private val dao: RecipeDao, private val foodWeekClient: FoodWeekClient) {

    fun getLiveDataRecipesByIds(ids: List<Long>): LiveData<List<Recipe>> {
        return dao.getRecipes(ids)
    }

    fun getLiveDataRecipeById(id: Long): LiveData<Recipe> {
        val recipeFromRoom = dao.getRecipe(id)
        if(recipeFromRoom.value != null){
            return recipeFromRoom
        }

        val liveDataRecipe = MutableLiveData<Recipe>()
        foodWeekClient.getFoodWeekServiceClient().getRecipe(id).enqueue(object : Callback,
            retrofit2.Callback<Recipe> {
            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                Log.println(
                    Log.ERROR,
                    "RecipeRepository",
                    "HTTP-Request /recipes/$id failed: ${t.message}"
                )
            }

            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                if(response.isSuccessful){
                    Log.println(Log.INFO, "RecipeRepository", "HTTP-Request /recipes/$id was successful: ${response.code()}")
                    liveDataRecipe.postValue(response.body())
                }else{
                    Log.println(Log.INFO, "RecipeRepository", "HTTP-Request /recipes/$id was not successful: ${response.code()}")
                }
            }

        })
        return liveDataRecipe
    }

    fun getRecipeById(id: Long): Recipe {
        val result = foodWeekClient.getFoodWeekServiceClient().getRecipe(id).execute()
        if(!result.isSuccessful){
            Log.println(Log.ERROR, "RecipeRepository", "Failed retrieving Recipe from Backend Database")
        }
        return result.body()!!
    }

    suspend fun createRecipe(recipe: Recipe) {
        dao.createRecipe(recipe)
    }
}