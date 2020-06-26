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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import retrofit2.Call
import retrofit2.Response
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
                            "(getMealPlans $page,$size,$query) HTTP-Request /mealplans failed: ${t.message}"
                        )
                    }

                    override fun onResponse(
                        call: Call<MealPlanResponse>,
                        response: Response<MealPlanResponse>
                    ) {
                        if (response.isSuccessful) {
                            val foundMealPlans = response.body()?.mealPlans ?: emptyList()
                            liveDataMealPlan.postValue(foundMealPlans)
                        } else {
                            Log.println(
                                Log.ERROR,
                                "MealPlanRepository",
                                "(getMealPlans $page,$size,$query)HTTP-Request /mealplans was not successful: ${response.code()}"
                            )
                        }
                    }
                })
        return liveDataMealPlan
    }

    fun getLiveDataMealPlanById(mealplanId: Long): LiveData<MealPlan> {
        Log.i("MealPlanRepository", "getLiveDataMealPlan by id: ${mealplanId}")

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
                                    "(getMealPlan $mealplanId) HTTP-Request /mealplans/$mealplanId failed: ${t.message}"
                                )
                            }

                            override fun onResponse(
                                call: Call<MealPlan>,
                                response: Response<MealPlan>
                            ) {
                                if (response.isSuccessful) {
                                    liveDataMealPlan.postValue(response.body())
                                } else {
                                    Log.println(
                                        Log.ERROR,
                                        "MealPlanRepository",
                                        "(getMealPlan $mealplanId) HTTP-Request /mealplans/$mealplanId was not successful: ${response.code()}"
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
        deleteMealPlanLocal(mealPlan)
        return if (mealPlan.draft) {
            return Completable.complete()
        } else {
            Completable.fromObservable(
                foodWeekClient.getFoodWeekServiceClient()
                    .deleteMealPlan(mealPlanId = mealPlan.planId, userId = userProvider.getUserID())
            )
        }
    }

    suspend fun deleteMealPlanLocal(mealPlan: MealPlan) {
        Log.i("MealPlanRepository", "delete local mealplan: ${mealPlan.planId}")
        dao.deleteMealPlan(mealPlan)
    }

    suspend fun draftNewMealPlan(mealPlan: MealPlan) {
        Log.i("MealPlanRepository", "new draft (id will change by DB): ${mealPlan.planId}")
        dao.createMealPlan(mealPlan)
    }

    suspend fun publishNewMealPlan(mealPlan: MealPlan) {
        Log.i("MealPlanRepository", "publish (id will change by Backend): ${mealPlan.planId}")
        //set planId to 0, to ensure to create a new MealPlan by the backend
        mealPlan.planId = 0
        val call = foodWeekClient.getFoodWeekServiceClient()
            .publishMealPlan(mealPlan, userProvider.getUserID())
        call.enqueue(object : retrofit2.Callback<MealPlan> {
            override fun onFailure(call: Call<MealPlan>, t: Throwable) {
                Log.w("MealPlanRepository", "create MealPlan did not work")
            }

            override fun onResponse(call: Call<MealPlan>, response: Response<MealPlan>) {
                Log.i("MealPlanRepository", "create local Entry by response.body : ${response.body()}")
                response.body()?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.createMealPlan(it)
                        Log.i("MealPlanRepository", " local Entry created")
                    }
                }
            }
        })
    }

    suspend fun updateMealPlanLocaly(mealPlan: MealPlan) {
        Log.i("MealPlanRepository", "update mealplan local: ${mealPlan.planId}")
        dao.updateMealPlan(mealPlan)
    }

    suspend fun updatePublishedMealPlan(mealPlan: MealPlan) {
        Log.i("MealPlanRepository", "update mealplan in Backend: ${mealPlan.planId}")
        val call = foodWeekClient.getFoodWeekServiceClient().updateMealPlan(mealPlanId = mealPlan.planId, mealPlan = mealPlan, userId = userProvider.getUserID())
        call.enqueue(object : retrofit2.Callback<MealPlan> {
            override fun onFailure(call: Call<MealPlan>, t: Throwable) {
                Log.w("MealPlanRepository", "update did not work")
            }

            override fun onResponse(call: Call<MealPlan>, response: Response<MealPlan>) {
                Log.i("MealPlanRepository", "update local Entry by response: ${response.body()}")
                response.body()?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.updateMealPlan(it)
                        Log.i("MealPlanRepository", " local Entry updated")
                    }
                }
            }
        })
    }
}