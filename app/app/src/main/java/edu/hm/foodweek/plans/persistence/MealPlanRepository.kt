package edu.hm.foodweek.plans.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.util.amplify.MealPlanContent
import edu.hm.foodweek.util.amplify.FoodWeekClient
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


open class MealPlanRepository(private val dao: MealPlanDao) : KoinComponent {

    private val foodWeekClient: FoodWeekClient by inject()

    fun getLiveDataAllMealPlans(): LiveData<List<MealPlan>> {
        val liveDataMealPlan = MutableLiveData<List<MealPlan>>()
        foodWeekClient.getFoodWeekServiceClient().getMealPlans(0, 20).enqueue(object : Callback,
            retrofit2.Callback<MealPlanContent> {
            override fun onFailure(call: Call<MealPlanContent>, t: Throwable) {
                Log.v("MealPlanRepository", "HTTP-Request /mealplans failed: ${t.message}")
            }

            override fun onResponse(call: Call<MealPlanContent>, response: Response<MealPlanContent>) {
                liveDataMealPlan.value = response.body()?.mealPlans
            }

        })
        return liveDataMealPlan
    }

    fun getLiveDataMealPlanById(mealplanId: Long): LiveData<MealPlan> {
        val mealplanlivedata = dao.getMealPlan(mealplanId)
        return mealplanlivedata
    }

    fun getMealPlanById(mealplanId: Long): MealPlan {
        return dao.getMealPlanNoLiveData(mealplanId)
    }

    fun getMealPlanCreatedByUser(userId: String): LiveData<List<MealPlan>> {
        return dao.getMealPlanCreatedByUser(userId)
    }

    fun getMealPlanCreatedByUserNoLiveData(userId: Long): List<MealPlan> {
        return dao.getMealPlanCreatedByUserNoLiveData(userId)
    }

    suspend fun createMealPlan(mealPlan: MealPlan) {
        dao.createMealPlan(mealPlan)
    }

    suspend fun deleteMealPlan(mealPlan: MealPlan) {
        return dao.deleteMealPlan(mealPlan)
    }

    suspend fun updateMealPlan(mealPlan: MealPlan) {
        return dao.updateMealPlan(mealPlan)
    }

}