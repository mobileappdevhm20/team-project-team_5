package edu.hm.foodweek.settings.screen

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.Meal
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.persistence.model.Recipe
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {

    val allMealPlans = mealPlanRepository.getAllMealPlans()

    val allMealPlanNames = Transformations.map(allMealPlans) {
        it.map { mealplan -> mealplan.title }
    }

    val selectedIndex = MutableLiveData<Int>().apply { value = 0 }

    val selectedMeal: LiveData<MealPlan?> = Transformations.map(selectedIndex) { index ->
        val allMealPlans = allMealPlans.value
        Log.d("SettingsViewModel", "allMP : $allMealPlans , index: $index")
        if (!allMealPlans.isNullOrEmpty() && index != null)
            allMealPlans.get(index)
        else
            null
    }

    val allRecipes = recipeRepository.getAllRecipes()

    val selectedRecipes = Transformations.map(selectedMeal) { mealplan ->
        val recipes = allRecipes.value
        return@map if (!recipes.isNullOrEmpty() && mealplan != null) {
            val recipeIds = mealplan.meals.map { meal -> meal.recipeId }
            recipes.filter { it.recipeId in recipeIds }
        } else {
            emptyList()
        }
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