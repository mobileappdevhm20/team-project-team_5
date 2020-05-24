package edu.hm.foodweek.week.screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.recipes.persistence.RecipeRepository

class WeekViewModelFactory(
    val mealPlanRepository: MealPlanRepository,
    val recipeRepository: RecipeRepository,
    val application: Application
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeekViewModel(mealPlanRepository, recipeRepository, application) as T
    }
}
