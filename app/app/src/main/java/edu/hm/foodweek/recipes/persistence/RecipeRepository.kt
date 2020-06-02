package edu.hm.foodweek.recipes.persistence

import androidx.lifecycle.LiveData
import edu.hm.foodweek.plans.persistence.RecipeDao
import edu.hm.foodweek.recipes.persistence.model.Recipe

class RecipeRepository(private val dao: RecipeDao) {

    fun getAllRecipes(): LiveData<List<Recipe>> {
        return dao.getAllRecipe()
    }

    fun getLiveDataRecipeById(id: Long): LiveData<Recipe> {
        return dao.getRecipe(id)
    }

    suspend fun getRecipeById(id: Long): Recipe {
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
}