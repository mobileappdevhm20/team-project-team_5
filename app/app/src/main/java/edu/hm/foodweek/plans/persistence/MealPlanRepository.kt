package edu.hm.foodweek.plans.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.util.amplify.FoodWeekClient
import edu.hm.foodweek.util.amplify.MealPlanResponse
import org.koin.core.KoinComponent
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


open class MealPlanRepository(private val dao: MealPlanDao, private val foodWeekClient: FoodWeekClient) : KoinComponent {

    fun getLiveDataAllMealPlans(
        query: String?,
        page: Int = 0,
        size: Int = 10
    ): LiveData<List<MealPlan>> {
        val liveDataMealPlan = MutableLiveData<List<MealPlan>>()
        Log.i("MealPlanRepository", "requested new MealPlans with query: $query")
        foodWeekClient
            .getFoodWeekServiceClient()
            .getMealPlans(page, size, query)
            .enqueue(
                object : Callback,
                    retrofit2.Callback<MealPlanResponse> {
                    override fun onFailure(call: Call<MealPlanResponse>, t: Throwable) {
                        Log.println(
                            Log.ERROR,
                            "MealPlanRepository",
                            "HTTP-Request /mealplans failed: ${t.message}"
                        )
                    }

                    override fun onResponse(call: Call<MealPlanResponse>, response: Response<MealPlanResponse>) {
                        Log.println(Log.INFO, "MealPlanRepository", "HTTP-Request /mealplans was successful: ${response.code()}")
                        val foundMealPlans = response.body()?.mealPlans ?: emptyList()
                        liveDataMealPlan.postValue(foundMealPlans)
                    }
                })
        return liveDataMealPlan
    }

    fun getLiveDataMealPlanById(mealplanId: Long): LiveData<MealPlan> {
        return dao.getMealPlan(mealplanId)
    }

    fun getMealPlanCreatedByUser(userId: String): LiveData<List<MealPlan>> {
        return dao.getMealPlanCreatedByUser(userId)
    }

    suspend fun createMealPlan(mealPlan: MealPlan) {
        dao.createMealPlan(mealPlan)
    }
}