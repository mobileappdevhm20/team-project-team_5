package edu.hm.foodweek.settings.screen

import android.app.Application
import androidx.lifecycle.*
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.Meal
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.util.extensions.combineLatest
import edu.hm.foodweek.util.extensions.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {

    val allMealPlans = mealPlanRepository.getLiveDataAllMealPlans()

    val selectedIndex = MutableLiveData<Int>().apply { value = 0 }

    val selectedMealPlan = selectedIndex.combineLatest(allMealPlans).map {
        val index = it?.first
        val mealPlans = it?.second
        return@map if (index != null && !mealPlans.isNullOrEmpty()) {
            mealPlans[index]
        } else {
            null
        }
    }

    val selectedRecipes = selectedMealPlan.switchMap { mealplan ->
        liveData {
            var recipesOfThisMealPlan = emptyList<Recipe>()
            if (mealplan != null && !mealplan.meals.isNullOrEmpty()) {
                recipesOfThisMealPlan = loadRecipes(mealplan.meals.map { meal -> meal.recipeId })
            }
            emit(recipesOfThisMealPlan)
        }
    }


    /**
     * Load recipes from database
     */
    private suspend fun loadRecipes(recipeIds: List<Long>): List<Recipe> = withContext(Dispatchers.IO) {
        var recipes = emptyList<Recipe>()
        recipeIds.forEach {
            val loadedRecipe = recipeRepository.getRecipeById(it)
            recipes = recipes.plus(loadedRecipe)
        }
        return@withContext recipes
    }

    fun createMeal() {
        viewModelScope.launch {
            recipeRepository.createRecipe(Recipe(0, "foo", "foo part twoo"))
            mealPlanRepository.createMealPlan(
                MealPlan(
                    0,
                    "hello World",
                    "description",
                    "imgUrl",
                    0,
                    true,
                    listOf(Meal(WeekDay.MONDAY, MealTime.BREAKFAST, 1L))
                )
            )
        }
    }
}