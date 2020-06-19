package edu.hm.foodweek.plans.screen.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.persistence.model.Recipe

class PlanDetailsViewModel(
    mealPlanId: Long,
    mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository,
    application: Application
) : AndroidViewModel(application) {

    private val mealPlan = mealPlanRepository.getLiveDataMealPlanById(mealPlanId)

    private val groupedPlans = mealPlan
        // Load recipe for each meal
        //.switchMap { mealPlan -> liveData { emit(loadRecipes(mealPlan)) } }
        .map { mealPlan -> mealPlan.meals.map { Pair(it, it.recipe) } }
        // Group by day
        .map { mealsWithRecipe -> mealsWithRecipe.groupBy { it.first.day } }
        // Group by time for each day
        .map { byDay -> byDay.mapValues { mealsWithRecipe -> mealsWithRecipe.value.groupBy { it.first.time } } }

    val items: LiveData<List<PlanDetailsItem>> = groupedPlans
        // Only use the first recipe on each time slot per day
        .map { byDayAndTime ->
            byDayAndTime.mapValues { byTime ->
                byTime.value.mapValues { mealsWithRecipe ->
                    mealsWithRecipe.value.map { it.second }.first()
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
                    PlanDetailsItem(
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
                .sortedWith(compareBy { it.day.ordinal })
        }

    fun detailedItems(d: WeekDay): LiveData<List<PlanDayDetailsItem>> {
        return groupedPlans
            .map { byDayAndTime ->
                byDayAndTime.getOrElse(d, { emptyMap() })
                    .map { byTime ->
                        PlanDayDetailsItem(
                            day = d,
                            time = byTime.key,
                            recipes = byTime.value.map { mealAndRecipe -> mealAndRecipe.second }
                        )
                    }
                    .sortedWith(compareBy({ it.day.ordinal }, { it.time.ordinal }))
            }
    }
}