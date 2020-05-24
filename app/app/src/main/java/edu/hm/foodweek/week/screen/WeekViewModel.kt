package edu.hm.foodweek.week.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.Meal
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import kotlinx.coroutines.launch

class WeekViewModel(private val mealPlanRepository: MealPlanRepository, application: Application) :
    AndroidViewModel(
        application
    ) {
    val allMealPlans = mealPlanRepository.getAllMealPlans()

    fun createOnClick() {
        viewModelScope.launch {
            mealPlanRepository.createMealPlan(
                MealPlan(
                    0,
                    "hello World",
                    "description",
                    "imgUrl",
                    0,
                    true,
                    listOf(Meal(WeekDay.MONDAY,MealTime.BREAKFAST,"1"))
                )
            )
        }
    }

}