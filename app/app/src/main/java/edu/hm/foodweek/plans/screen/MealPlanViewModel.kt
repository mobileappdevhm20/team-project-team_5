package edu.hm.foodweek.plans.screen

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.screen.browse.BrowseableMealPlan
import edu.hm.foodweek.util.extensions.combineLatest
import edu.hm.foodweek.util.extensions.debounce
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MealPlanViewModel(
    private val mealPlanRepository: MealPlanRepository,
    application: Application
) : AndroidViewModel(application) {

    private val disposables = CompositeDisposable()

    init {
        disposables.add(
            mealPlanRepository.getSubscribedMealPlans()
                .subscribe(
                    { _subscribedPlans.postValue(it) },
                    { Log.e("MealPlanViewModel", "Unable to get subscribed plans", it) })
        )
        disposables.add(
            mealPlanRepository.getOwnMealPlans()
                .subscribe(
                    { _ownPlans.postValue(it) },
                    { Log.e("MealPlanViewModel", "Unable to get owned plans", it) })
        )
    }

    private val _subscribedPlans = MutableLiveData<List<MealPlan>>(emptyList())
    private val _ownPlans = MutableLiveData<List<MealPlan>>(emptyList())

    val filterText = MutableLiveData<String>("")

    val managedPlans = _ownPlans
        .combineLatest(_subscribedPlans)
        .map { combined ->
            combined.first.map { plan ->
                BrowseableMealPlan(
                    plan = plan,
                    subscribed = combined.second.any { it.planId == plan.planId },
                    owned = true
                )
            }.plus(combined.second.map {
                BrowseableMealPlan(
                    plan = it,
                    subscribed = true
                )
            })
        }

    val browsablePlans = filterText
        .debounce(1000, viewModelScope)
        .switchMap { mealPlanRepository.getLiveDataAllMealPlans(it) }
        .combineLatest(managedPlans)
        .map { combined ->
            combined.first.map { plan ->
                BrowseableMealPlan(
                    plan = plan,
                    subscribed = combined.second.any { it.plan.planId == plan.planId }
                )
            }
        }

    fun deletePlan(mealPlan: MealPlan) {
        val uiScope = CoroutineScope(Dispatchers.IO)
        uiScope.launch {
            mealPlanRepository.deleteMealPlan(mealPlan)
                .subscribe({}, { error ->
                    Log.e("MealPlanViewModel", "Unable to delete meal plan", error)
                }, {
                    _ownPlans.postValue(_ownPlans.value?.minus(mealPlan))
                })
        }
    }

    fun subscribePlan(mealPlan: MealPlan) {
        disposables.add(
            mealPlanRepository.subscribeMealPlan(mealPlan.planId)
                .subscribe({
                    _subscribedPlans.postValue(_subscribedPlans.value?.plus(it))
                }, {
                    Log.e("MealPlanViewModel", "Unable to subscribe plan ${mealPlan.planId}", it)
                })
        )
    }

    fun unsubscribePlan(mealPlan: MealPlan) {
        disposables.add(mealPlanRepository.unsubscribeMealPlan(mealPlan.planId)
            .subscribe({
                _subscribedPlans.postValue(_subscribedPlans.value?.minus(it))
            }, {
                Log.e("MealPlanViewModel", "Unable to unsubscribe plan ${mealPlan.planId}", it)
            })
        )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}