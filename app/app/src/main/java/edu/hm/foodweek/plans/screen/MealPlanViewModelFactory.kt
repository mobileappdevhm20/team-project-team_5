package edu.hm.foodweek.plans.screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.hm.foodweek.plans.persistence.MealPlanRepository

class MealPlanViewModelFactory(
    private val mealPlanRepository: MealPlanRepository,
    val application: Application
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MealPlanViewModel(
            mealPlanRepository,
            application
        ) as T
    }
}
