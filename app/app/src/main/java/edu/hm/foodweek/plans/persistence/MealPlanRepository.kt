package edu.hm.foodweek.plans.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.util.UserProvider
import edu.hm.foodweek.util.amplify.FoodWeekClient
import edu.hm.foodweek.util.amplify.response.MealPlanResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import retrofit2.Call
import retrofit2.Response
import java.util.logging.Logger
import javax.security.auth.callback.Callback

open class MealPlanRepository(
    private val dao: MealPlanDao,
    private val userProvider: UserProvider,
    private val foodWeekClient: FoodWeekClient
) : KoinComponent {
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

                    override fun onResponse(
                        call: Call<MealPlanResponse>,
                        response: Response<MealPlanResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.println(
                                Log.INFO,
                                "MealPlanRepository",
                                "HTTP-Request /mealplans was successful: ${response.code()}"
                            )
                            val foundMealPlans = response.body()?.mealPlans ?: emptyList()
                            liveDataMealPlan.postValue(foundMealPlans)
                        } else {
                            Log.println(
                                Log.ERROR,
                                "MealPlanRepository",
                                "HTTP-Request /mealplans was not successful: ${response.code()}"
                            )
                        }
                    }
                })
        return liveDataMealPlan
    }

    fun getLiveDataMealPlanById(mealplanId: Long): LiveData<MealPlan> {

        return dao.getMealPlan(mealplanId)
            .switchMap {
                return@switchMap if (it != null) {
                    liveData { emit(it!!) }
                } else {
                    val liveDataMealPlan = MutableLiveData<MealPlan>()
                    foodWeekClient.getFoodWeekServiceClient().getMealPlan(mealplanId)
                        .enqueue(object : Callback,
                            retrofit2.Callback<MealPlan> {
                            override fun onFailure(call: Call<MealPlan>, t: Throwable) {
                                Log.println(
                                    Log.ERROR,
                                    "MealPlanRepository",
                                    "HTTP-Request /mealplans/$mealplanId failed: ${t.message}"
                                )
                            }

                            override fun onResponse(
                                call: Call<MealPlan>,
                                response: Response<MealPlan>
                            ) {
                                if (response.isSuccessful) {
                                    Log.println(
                                        Log.INFO,
                                        "MealPlanRepository",
                                        "HTTP-Request /mealplans/$mealplanId was successful: ${response.code()}"
                                    )
                                    liveDataMealPlan.postValue(response.body())
                                } else {
                                    Log.println(
                                        Log.ERROR,
                                        "MealPlanRepository",
                                        "HTTP-Request /mealplans/$mealplanId was not successful: ${response.code()}"
                                    )
                                }
                            }

                        })
                    liveDataMealPlan
                }
            }
    }

    fun getOwnMealPlans(): LiveData<List<MealPlan>> {
        return dao.getAllMealPlans()
    }

    fun getSubscribedMealPlans(): Observable<List<MealPlan>> {
        return foodWeekClient
            .getFoodWeekServiceClient()
            .getSubscribedMealplans(userProvider.getUserID(), userProvider.getUserID())
            .subscribeOn(Schedulers.io())
    }

    fun subscribeMealPlan(id: Long): Observable<MealPlan> {
        return foodWeekClient
            .getFoodWeekServiceClient()
            .subscribeMealPlan(userProvider.getUserID(), id, userProvider.getUserID())
            .subscribeOn(Schedulers.io())
    }

    fun unsubscribeMealPlan(id: Long): Observable<MealPlan> {
        return foodWeekClient
            .getFoodWeekServiceClient()
            .unsubscribeMealPlan(userProvider.getUserID(), id, userProvider.getUserID())
            .subscribeOn(Schedulers.io())
    }

    suspend fun deleteMealPlan(mealPlan: MealPlan): Completable {
        dao.deleteMealPlan(mealPlan)
        return if (mealPlan.draft) {
            return Completable.complete()
        } else {
            Completable.fromObservable(
                foodWeekClient.getFoodWeekServiceClient()
                    .deleteMealPlan(mealPlanId = mealPlan.planId, userId = userProvider.getUserID())
            )
        }
    }

    suspend fun draftNewMealPlan(mealPlan: MealPlan) {
        dao.createMealPlan(mealPlan)
        Log.i("MealPlanRepository", "saved draft of MealPlan")
    }

    fun publishNewMealPlan(mealPlan: MealPlan) {
        val call = foodWeekClient.getFoodWeekServiceClient()
            .publishMealPlan(mealPlan, userProvider.getUserID())
        val response = call.execute()
        if (response.isSuccessful) {
            Logger.getLogger("MealPlanRepository").fine("published new MealPlan")
        } else {
            Logger.getLogger("MealPlanRepository")
                .warning("unable to publish MealPlan - response: $response")
        }
    }
}