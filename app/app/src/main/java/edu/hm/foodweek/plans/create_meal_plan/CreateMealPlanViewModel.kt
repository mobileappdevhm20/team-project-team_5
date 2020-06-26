package edu.hm.foodweek.plans.create_meal_plan

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.Meal
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.util.UserProvider
import edu.hm.foodweek.util.extensions.combineLatest
import edu.hm.foodweek.util.extensions.debounce
import edu.hm.foodweek.util.extensions.map
import edu.hm.foodweek.util.extensions.mapSkipNulls
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class CreateMealPlanViewModel(
    val mealPlanId: Long,
    val mealPlanRepository: MealPlanRepository,
    val recipeRepository: RecipeRepository,
    val userProvider: UserProvider,
    application: Application
) : AndroidViewModel(application) {
    var selectedDay: MutableLiveData<WeekDay> =
        MutableLiveData<WeekDay>().apply { value = WeekDay.MONDAY }
    var selectTime: MutableLiveData<MealTime> =
        MutableLiveData<MealTime>().apply { value = MealTime.BREAKFAST }
    val selectedRecipe = MutableLiveData<Recipe?>()
    var _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery.debounce(400, viewModelScope)
    val allRecipes = recipeRepository.recipes

    // mealplan

    private var _currentEditingMealPlan: MutableLiveData<MealPlan>
    private val editExisting = mealPlanId > 0

    init {
        _currentEditingMealPlan = if (editExisting) {
            mealPlanRepository.getLiveDataMealPlanById(mealPlanId) as MutableLiveData
        } else {
            MutableLiveData(
                MealPlan(
                    planId = 0L,
                    creatorId = userProvider.getUserID(),
                    title = "",
                    description = "",
                    imageURL = "",
                    draft = true,
                    meals = emptyList()
                )
            )
        }
    }

    val currentEditingMealPlan = _currentEditingMealPlan as LiveData<MealPlan>

    private val meals = currentEditingMealPlan.mapSkipNulls { plan -> plan.meals }
    private val mealsFilterByTimeAndDay = meals
        .combineLatest(selectedDay)
        .map { pair ->
            pair?.first?.filter { meal ->
                meal.day == pair.second
            } ?: emptyList()
        }.combineLatest(selectTime).map { pair ->
            pair?.first?.filter { meal ->
                meal.time == pair.second
            } ?: emptyList()
        }

    val filteredRecipeList = mealsFilterByTimeAndDay.map { mealDto ->
        mealDto?.map {
            it.recipe
        }?.toList() ?: emptyList<Recipe>()
    }

    private fun saveMealPlan(mealPlan: MealPlan, existedBefore: Boolean, publish: Boolean) {
        Log.i("CreateMealPlanViewModel", "saveMealPlan ${mealPlan.planId}, existedBefore: $existedBefore, publish: $publish")
        if (mealPlan.meals.isEmpty() || mealPlan.title.isNullOrEmpty() || mealPlan.description.isNullOrEmpty()) {
            Log.w(
                "CreateMealPlanViewModel",
                "could not saveMealPlan: $mealPlan"
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            runBlocking {
                if (!existedBefore) {
                    //save localy first time
                    if (!publish) {
                        mealPlanRepository.draftNewMealPlan(mealPlan)
                    }
                } else {
                    // update Local Entry
                    if (!publish) {
                        mealPlanRepository.updateMealPlanLocaly(mealPlan)
                    }
                }
                if (publish) {
                    // patch online Entry
                    if (mealPlan.draft) {
                        mealPlanRepository.deleteMealPlanLocal(mealPlan)
                        //create Online Entry
                        mealPlan.draft = false
                        mealPlanRepository.publishNewMealPlan(mealPlan)
                        // when online Entry is created, local entry is deleted and new Entry is created
                    } else {
                        mealPlanRepository.updatePublishedMealPlan(mealPlan)
                    }
                }
            }
        }
    }


    fun saveDraft() {
        val mealplan = currentEditingMealPlan.value
        val allMeals = meals.value
        mealplan?.let {
            it.meals = allMeals.orEmpty()
            saveMealPlan(it, editExisting, false)
        }
    }

    fun publishMealPlan() {
        val mealplan = currentEditingMealPlan.value
        mealplan?.let {
            saveMealPlan(it, editExisting, true)
        }
    }

    fun updateTexts(title: String? = null, description: String? = null, url: String? = null) {
        val existingMealPlan = currentEditingMealPlan.value
        if (existingMealPlan != null) {
            existingMealPlan.let {
                if (description != null) {
                    it.description = description
                }
                if (title != null) {
                    it.title = title
                }
                if (url != null) {
                    it.imageURL = url
                }
            }
            _currentEditingMealPlan.postValue(existingMealPlan)
        }
    }

    fun addMeal(recipe: Recipe) {
        val existingMeals = meals.value

        val currentList = mutableListOf<Meal>()
        if (existingMeals != null) {
            currentList.addAll(existingMeals)
        }
        val currentSelectedDay = selectedDay.value
        val currentSelectedTime = selectTime.value

        if (currentSelectedDay != null && currentSelectedTime != null) {
            currentList.add(
                Meal(
                    day = currentSelectedDay,
                    time = currentSelectedTime,
                    recipe = recipe
                )
            )
        } else {
            Log.w("CreateMealPlanViewModel", "could not insert meal because: $currentList, $currentSelectedDay, $recipe")
        }

        val existingMealPlan = currentEditingMealPlan.value
        if (existingMealPlan != null) {
            existingMealPlan.meals = currentList
            _currentEditingMealPlan.postValue(existingMealPlan)
        }
    }

    fun loadMoreRecipes(page: Int) {
        val query = searchQuery.value
        recipeRepository.getAllRecipes(page = page, size = 30, query = query)
        val currentRecipes = allRecipes.value ?: emptyList()
        Log.i("CreateMealPlanViewModel", "update Recipes from ${currentRecipes.size} with query ${searchQuery.value}")
    }

    fun removeMeal(recipeId: Long) {
        val time = selectTime.value
        val day = selectedDay.value
        val existingMeals = meals.value
        if (time != null && day != null && existingMeals != null) {
            val removingMeal = existingMeals.filter { meal ->
                meal.day == day && meal.time == time && meal.recipe.recipeId == recipeId
            }.getOrNull(0)
            if (removingMeal == null) {
                Log.w("CreateMealPlanViewModel", "meal to remove was null")
            }
            removingMeal?.let { meal ->
                val existingMealPlan = currentEditingMealPlan.value
                existingMealPlan?.let {
                    it.meals = existingMeals.minus(meal)
                    _currentEditingMealPlan.postValue(existingMealPlan)
                }
            }
        } else {
            Log.w("CreateMealPlanViewModel", "could not remove $recipeId, because time($time) or day($day) or existingMeals($existingMeals) are null")
        }
    }
}