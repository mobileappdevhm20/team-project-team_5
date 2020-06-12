package edu.hm.foodweek.settings.screen

import android.app.Application
import androidx.lifecycle.*
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.Meal
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.persistence.model.Ingredient
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.recipes.persistence.model.Unit
import edu.hm.foodweek.recipes.persistence.model.UnitScale
import edu.hm.foodweek.users.persistence.UserRepository
import edu.hm.foodweek.util.extensions.combineLatest
import edu.hm.foodweek.util.extensions.map
import edu.hm.foodweek.util.extensions.mapSkipNulls
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository,
    private val userRepository: UserRepository,
    application: Application
) : AndroidViewModel(application) {

    val text = "This is the Settings fragment"

    val allMealPlans = mealPlanRepository.getLiveDataAllMealPlans(null)

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
            recipeRepository.createRecipe(
                Recipe(
                    0,
                    "Tomato Dipp with Bread",
                    "The best way to use old tomatoes",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Recipe_Unlimited_logo.png/320px-Recipe_Unlimited_logo.png",
                    arrayListOf(
                        Ingredient("tomato", Unit(3, UnitScale.Pieces)),
                        Ingredient("Bread", Unit(1, UnitScale.KiloGram))
                    ),
                    arrayListOf(
                        "munch those lucky tomatoes",
                        "heat them in the microwave for 3 minutes",
                        "cut bread into slices",
                        "toast them",
                        "dipp them in the tomato sausage",
                        "enjoy"
                    )
                )
            )
            runBlocking {
                val userid = userRepository.getUserNoLiveData().userId
                mealPlanRepository.createMealPlan(
                    MealPlan(
                        0,
                        "hello World",
                        "description",
                        "imgUrl",
                        userid,
                        true,
                        listOf(Meal(WeekDay.MONDAY, MealTime.BREAKFAST, 1L))
                    )
                )
            }
        }
    }
}