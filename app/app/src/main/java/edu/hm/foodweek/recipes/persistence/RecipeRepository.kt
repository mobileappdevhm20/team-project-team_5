package edu.hm.foodweek.recipes.persistence

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.hm.foodweek.recipes.persistence.model.Recipe


class RecipeRepository {
    private var recipes: MutableList<Recipe> = mutableListOf()
    private var _recipes: MutableLiveData<List<Recipe>> = MutableLiveData(recipes)

    fun getAllRecipes(): LiveData<List<Recipe>> {
        return _recipes
    }

    fun createRecipe(recipe: Recipe): Boolean{
        val created = recipes.add(recipe)
        _recipes.postValue(recipes)
        return created
    }

    fun deleteRecipe(recipe: Recipe): Boolean {
        val removed = recipes.remove(recipe)
        _recipes.postValue(recipes)
        return removed
    }

    fun updateRecipe(recipe: Recipe): Recipe {
        recipes[recipes.indexOf(recipe)] = recipe
        _recipes.postValue(recipes)
        return recipe
    }


    companion object {
        private var instance: RecipeRepository? = null

        @Synchronized
        private fun createInstance() {
            if (instance == null) {
                instance =
                    RecipeRepository()
            }
        }

        fun getInstance(context: Context): RecipeRepository {
            if (instance == null) createInstance()
            return instance!!
        }
    }
}