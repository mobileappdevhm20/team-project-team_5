package edu.hm.foodweek.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class RecipeRepository {
    private var instance: RecipeRepository? = null
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

    @Synchronized
    private fun createInstance() {
        if (instance == null) {
            instance = RecipeRepository()
        }
    }

    fun getInstance(): RecipeRepository {
        if (instance == null) createInstance()
        return instance!!
    }
}