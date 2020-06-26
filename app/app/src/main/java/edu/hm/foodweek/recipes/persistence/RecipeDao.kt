package edu.hm.foodweek.recipes.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.hm.foodweek.recipes.persistence.model.Recipe

@Dao
interface RecipeDao {

    @Transaction
    @Query("SELECT * FROM Recipe")
    fun getAllRecipe(): LiveData<List<Recipe>>

    @Transaction
    @Query("SELECT * FROM Recipe where recipeId = :id")
    fun getRecipe(id: Long): LiveData<Recipe>

    @Transaction
    @Query("SELECT * FROM RECIPE where recipeId IN(:ids)")
    fun getRecipes(ids: List<Long>): LiveData<List<Recipe>>


    @Query("SELECT * FROM Recipe where recipeId = :id")
    suspend fun getRecipeNoLiveData(id: Long): Recipe

    @Insert
    suspend fun createRecipe(recipe: Recipe): Long

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)
}
