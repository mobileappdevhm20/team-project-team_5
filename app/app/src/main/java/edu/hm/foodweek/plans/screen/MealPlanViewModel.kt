package edu.hm.foodweek.plans.screen


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import edu.hm.foodweek.plans.persistence.MealPlanRepository

class MealPlanViewModel(
    mealPlanRepository: MealPlanRepository,
    application: Application
) : AndroidViewModel(application) {
    val allMealPlans = mealPlanRepository.getLiveDataAllMealPlans()
    val allMealPlansCreatedByUser = mealPlanRepository.getMealPlanCreatedByUser(0)
}