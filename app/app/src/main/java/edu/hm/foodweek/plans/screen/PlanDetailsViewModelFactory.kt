package edu.hm.foodweek.plans.screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.recipes.persistence.RecipeRepository

class PlanDetailsViewModelFactory(
    private val mealPlanId: Long,
    val mealPlanRepository: MealPlanRepository,
    val recipeRepository: RecipeRepository,
    val application: Application
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlanDetailsViewModel(
            mealPlanId,
            mealPlanRepository,
            recipeRepository,
            application
        ) as T
    }
}
