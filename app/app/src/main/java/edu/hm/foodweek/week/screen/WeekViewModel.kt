package edu.hm.foodweek.week.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import edu.hm.foodweek.plans.MealPlanRepository

class WeekViewModel(private val mealPlanRepository: MealPlanRepository, application: Application) : AndroidViewModel(
    application
) {

    val currentMealPlan = mealPlanRepository.getMealNow()
}