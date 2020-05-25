package edu.hm.foodweek.recipes.persistence

import android.content.Context
import androidx.lifecycle.LiveData
import edu.hm.foodweek.FoodWeekDatabase
import edu.hm.foodweek.recipes.persistence.model.Recipe


class RecipeRepository(context: Context) {
    val dao = FoodWeekDatabase.getInstance(context).recipeDao()
    fun getAllRecipes(): LiveData<List<Recipe>> {
        return dao.getAllRecipe()
    }

    fun getLiveDataRecipeById(id: Long): LiveData<Recipe> {
        return dao.getRecipe(id)
    }

    suspend fun getRecipeById(id:Long):Recipe{
        return dao.getRecipeNoLiveData(id)
    }

    suspend fun createRecipe(recipe: Recipe) {
        dao.createRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        deleteRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe) {
        dao.updateRecipe(recipe)
    }

    companion object {
        private var instance: RecipeRepository? = null

        @Synchronized
        private fun createInstance(context: Context) {
            if (instance == null) {
                instance =
                    RecipeRepository(context)
            }
        }

        fun getInstance(context: Context): RecipeRepository {
            if (instance == null) createInstance(context)
            return instance!!
        }
    }
}