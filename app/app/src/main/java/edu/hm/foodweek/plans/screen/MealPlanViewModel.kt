package edu.hm.foodweek.plans.screen


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.util.extensions.debounce

class MealPlanViewModel(
    val mealPlanRepository: MealPlanRepository,
    application: Application
) : AndroidViewModel(application) {
    val filterText = MutableLiveData<String>("")
    val allMealPlansCreatedByUser = mealPlanRepository.getMealPlanCreatedByUser("userId2")
    val filteredMealPlans = filterText
        .debounce(1000, viewModelScope)
        .switchMap { mealPlanRepository.getLiveDataAllMealPlans(it) }
}