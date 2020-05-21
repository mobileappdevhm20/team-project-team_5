package edu.hm.foodweek.plans

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.hm.foodweek.storage.User


class MealPlanRepository {

    private var mealPlans: MutableList<MealPlan> = mutableListOf()
    private var _mealPlans: MutableLiveData<List<MealPlan>> = MutableLiveData(mealPlans)

    val scheduledMealPlans: Map<Int, MealPlan> = emptyMap()

    fun getAllMealPlans(): LiveData<List<MealPlan>> {
        return _mealPlans
    }

    fun createMealPlan(mealPlan: MealPlan): Boolean {
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

    /*
    * leave until we have mocked data
    * */
    fun getMealNow(): LiveData<MealPlan> {
        return MutableLiveData<MealPlan>().apply { value = MealPlan("denis is the greatest", User()) }
    }

    companion object {
        private var instance: MealPlanRepository? = null

        @Synchronized
        private fun createInstance() {
            if (instance == null) {
                instance = MealPlanRepository()
            }
        }

        fun getInstance(context: Context): MealPlanRepository {
            if (instance == null) createInstance()
            return instance!!
        }
    }

}