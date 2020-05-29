package edu.hm.foodweek.plans.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.Meal
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.persistence.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlanDetailsViewModel(
    mealPlanId: Long,
    mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {

    private val mealPlan = mealPlanRepository.getLiveDataMealPlanById(mealPlanId)

    val items = mealPlan
        // Load recipe for each meal
        .switchMap { mealPlan -> liveData { emit(loadRecipes(mealPlan)) } }
        // Group by day
        .map { mealsWithRecipe -> mealsWithRecipe.groupBy { it.first.day } }
        // Group by time for each day
        .map { byDay -> byDay.mapValues { mealsWithRecipe -> mealsWithRecipe.value.groupBy { it.first.time } } }
        // Only use the first recipe on each time slot per day
        .map { byDayAndTime ->
            byDayAndTime.mapValues { byTime ->
                byTime.value.mapValues { mealsWithRecipe ->
                    mealsWithRecipe.value.filter { it.second.url.isNotBlank() }.map { it.second }
                        .first()
                }
            }
        }
        // Map to list
        .map { byDayAndTime ->
            byDayAndTime
                .map { days ->
                    val dummyURL =
                        "https://icons.iconarchive.com/icons/papirus-team/papirus-status/512/image-missing-icon.png"
                    val emptyRecipe = Recipe(0, "-", "-", url = dummyURL)
                    PlanTimelineItem(
                        day = days.key,
                        dishImageURL = days.value.getOrElse(
                            MealTime.LUNCH,
                            { emptyRecipe }).url,
                        breakfastTitle = days.value.getOrElse(
                            MealTime.BREAKFAST,
                            { emptyRecipe }).title,
                        lunchTitle = days.value.getOrElse(MealTime.LUNCH, { emptyRecipe }).title,
                        dinnerTitle = days.value.getOrElse(MealTime.DINNER, { emptyRecipe }).title
                    )
                }
        }

    private suspend fun loadRecipes(plan: MealPlan?): List<Pair<Meal, Recipe>> =
        withContext(Dispatchers.IO) {
            return@withContext plan?.meals?.map { meal ->
                Pair(
                    meal,
                    recipeRepository.getRecipeById(meal.recipeId)
                )
            }
                ?: emptyList()
        }
}