package edu.hm.foodweek.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class MealPlanRepository {
    private var instance: MealPlanRepository? = null
    private var mealPlans: MutableList<MealPlan> = mutableListOf()
    private var _mealPlans: MutableLiveData<List<MealPlan>> = MutableLiveData(mealPlans)

    fun getAllMealPlans(): LiveData<List<MealPlan>> {
        return _mealPlans
    }

    fun createMealPlan(mealPlan: MealPlan): Boolean{
        val created = mealPlans.add(mealPlan)
        _mealPlans.postValue(mealPlans)
        return created
    }

    fun deleteMealPlan(mealPlan: MealPlan): Boolean {
        val removed = mealPlans.remove(mealPlan)
        _mealPlans.postValue(mealPlans)
        return removed
    }

    fun updateMealPlan(mealPlan: MealPlan): MealPlan {
        mealPlans[mealPlans.indexOf(mealPlan)] = mealPlan
        _mealPlans.postValue(mealPlans)
        return mealPlan
    }

    @Synchronized
    private fun createInstance() {
        if (instance == null) {
            instance = MealPlanRepository()
        }
    }

    fun getInstance(): MealPlanRepository {
        if (instance == null) createInstance()
        return instance!!
    }
}