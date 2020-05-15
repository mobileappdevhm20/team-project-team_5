package edu.hm.foodweek.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class MealPlanRepository {
    private var instance: MealPlanRepository? = null

    fun observeAllMealPlans(): LiveData<MealPlan> {

        return MutableLiveData<MealPlan>()
    }


    @Synchronized
    private fun createInstance() {
        if (instance == null) {
            instance = MealPlanRepository()
        }
    }

    fun getInstance(): MealPlanRepository? {
        if (instance == null) createInstance()
        return instance
    }
}