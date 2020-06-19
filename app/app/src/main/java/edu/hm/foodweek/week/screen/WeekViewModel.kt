package edu.hm.foodweek.week.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.users.persistence.UserRepository
import edu.hm.foodweek.util.extensions.combineLatest
import edu.hm.foodweek.util.extensions.map
import edu.hm.foodweek.util.extensions.mapSkipNulls
import edu.hm.foodweek.week.MealPreview
import org.koin.android.logger.AndroidLogger

class WeekViewModel(
    private val mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository,
    userRepository: UserRepository,
    application: Application
) :
    AndroidViewModel(
        application
    ) {
    // first fetch the current Week id
    private val currentMealPlanId = userRepository.getCurrentWeek().mapSkipNulls {
        AndroidLogger().debug("$it")
        it
    }

    // check if there is a mealplan for this week
    val isMealPlanForCurrentWeekSet = currentMealPlanId.map { it != -1L }

    // load the mealplan
    private val mealPlan = currentMealPlanId.switchMap {
        if (it != -1L) {
            mealPlanRepository.getLiveDataMealPlanById(it)
        } else {
            // provide Default Mealplan, the values wont be used
            liveData { emit(MealPlan(0, "", "", "", "nouser", true, creatorUsername = "username")) }
        }
    }
    val planId = mealPlan.mapSkipNulls { it.planId }
    val planTitle = mealPlan
        .mapSkipNulls { it.title }
        .combineLatest(isMealPlanForCurrentWeekSet)
        .mapSkipNulls { pair ->
            if (pair.second == null || pair.second == true) {
                return@mapSkipNulls pair.first
            } else {
                return@mapSkipNulls "No MealPlan assigned for this Week"
            }
        }
    val planDescription = mealPlan.mapSkipNulls { it.description }
    val planUrl = mealPlan.mapSkipNulls { it.imageURL }

    private val recipes = mealPlan.switchMap {
        recipeRepository.getLiveDataRecipesByIds(it.meals.map { meal -> meal.recipe.recipeId })
    }

    private val recipeMap = recipes.mapSkipNulls { list ->
        list.map { recipe -> Pair<Long, Recipe>(recipe.recipeId, recipe) }.toMap()
    }

    val meals = recipeMap.combineLatest(mealPlan.mapSkipNulls { it.meals }).mapSkipNulls { pair ->
        val recipes = pair.first
        val meals = pair.second
        meals.map { meal ->
            val recipe = recipes[meal.recipe.recipeId]
            if (recipe != null)
                MealPreview(meal.time, recipe.title, meal.recipe.recipeId, recipe.url)
            else
                MealPreview(MealTime.BREAKFAST, "undefined", -1, "")
        }
    }

}