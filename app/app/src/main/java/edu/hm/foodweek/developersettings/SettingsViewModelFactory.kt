package edu.hm.foodweek.developersettings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.settings.screen.SettingsViewModel

class SettingsViewModelFactory(
    val mealPlanRepository: MealPlanRepository,
    val recipeRepository: RecipeRepository,
    val application: Application
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(mealPlanRepository, recipeRepository, application) as T
    }
}
