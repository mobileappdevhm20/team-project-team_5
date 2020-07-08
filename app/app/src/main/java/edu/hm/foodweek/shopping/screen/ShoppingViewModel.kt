package edu.hm.foodweek.shopping.screen

import androidx.lifecycle.*
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.persistence.model.Ingredient
import edu.hm.foodweek.recipes.persistence.model.IngredientAmount
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.users.persistence.UserRepository
import edu.hm.foodweek.util.extensions.combineLatest
import edu.hm.foodweek.util.extensions.mapSkipNulls
import edu.hm.foodweek.util.measureMappings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt


class ShoppingViewModel(
    private val userRepository: UserRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    val days = MutableLiveData<Int>(3)

    val ingredients: LiveData<List<IngredientAmount>?> = userRepository
        // Load meal plans
        .getCurrentWeek().switchMap {
            if (it != -1L) {
                mealPlanRepository.getLiveDataMealPlanById(it)
            } else {
                liveData { emit(MealPlan()) }
            }
        }
        .combineLatest(
            userRepository.getNextWeek()
                .switchMap {
                    if (it != -1L) {
                        mealPlanRepository.getLiveDataMealPlanById(it)
                    } else {
                        liveData { emit(MealPlan()) }
                    }
                })
        .combineLatest(days.distinctUntilChanged())
        .map { plansAndDays ->
            val days = plansAndDays.second
            var currentPlan = plansAndDays.first.first
            var nextPlan = plansAndDays.first.second


            // Compute day limits
            val today = WeekDay.valueOf(
                LocalDate().dayOfWeek().getAsText(Locale.US).toUpperCase(
                    Locale.ROOT
                )
            )
            val upperLimit = WeekDay.valueOf(
                LocalDate().plusDays(days).dayOfWeek().getAsText(Locale.US).toUpperCase(
                    Locale.ROOT
                )
            )

            // Filter days of current week
            val weekOverflow = today.ordinal + days > 6
            currentPlan = if (weekOverflow) {
                MealPlan(meals = currentPlan.meals.filter { it.day.ordinal >= today.ordinal })
            } else {
                MealPlan(meals = currentPlan.meals.filter { it.day.ordinal >= today.ordinal && it.day.ordinal < upperLimit.ordinal })
            }

            // Filter next week
            nextPlan =
                MealPlan(meals = nextPlan.meals.filter { it.day.ordinal < upperLimit.ordinal })

            // Combine meal plans
            val plans = if (weekOverflow) {
                mutableListOf(currentPlan, nextPlan)
            } else {
                mutableListOf(currentPlan)
            }
            plans
        }
        .switchMap { plans ->
            liveData {
                emit(plans
                    .filterNotNull()
                    .flatMap { plan -> loadRecipes(plan) })
            }
        }
        .mapSkipNulls { recipes ->
            recipes.map { it.ingredients }.flatten()
                .groupBy { ingredientAmount: IngredientAmount -> ingredientAmount.ingredient.name.toUpperCase() }
                .entries.map { entry ->
                    IngredientAmount(
                        ingredient = Ingredient(entry.key),
                        measure = combineMeasures(entry.value.map { ingredientAmount -> ingredientAmount.measure })
                    )
                }.sortedBy { it.ingredient.name }
        }

    fun daySelectionChanged(dayInput: Int) {
        days.postValue(dayInput)
    }

    private suspend fun loadRecipes(plan: MealPlan?): List<Recipe> =
        withContext(Dispatchers.IO) {
            return@withContext plan?.meals?.map { meal ->
                recipeRepository.getRecipeById(meal.recipe.recipeId)
            }
                ?: emptyList()
        }

    private fun combineMeasures(measures: List<String>): String {
        // Pre-processing of measures
        var cleanedMeasures = measures.map { measure ->
            var cleanedMeasure = measure.toLowerCase()
            measureMappings.entries.forEach { mapping ->
                cleanedMeasure = cleanedMeasure.replace(mapping.key, mapping.value)
            }
            cleanedMeasure
        }

        // Combination of same measures
        val combinedMeasures = mutableListOf<String>()
        val unitToAmount = mutableMapOf<String, Double>()
        var pieces = 0

        cleanedMeasures.forEach { input ->
            // Combines measures of pattern (amount)(measure)
            val match = Regex("(\\d+[\\.\\,]?\\d*)\\s*([^\\d\\W]+.*)").find(input)
            if (match != null) {
                var (amount, unit) = match.destructured
                unit = unit.split("/")[0]
                unitToAmount[unit] = unitToAmount.getOrElse(unit, { 0.0 }) + amount.toDouble()
            } else {
                // Combines peaces
                if (input.matches("\\d+".toRegex())) {
                    pieces += input.toInt()
                } else {
                    // Everything else cannot be combined
                    combinedMeasures.add(input)
                }
            }
        }

        // Add combined measures to ungrouped measures
        unitToAmount.entries.forEach { entry ->
            if (ceil(entry.value) == floor(entry.value)) {
                combinedMeasures.add("${entry.value.roundToInt()} ${entry.key}")
            } else {
                combinedMeasures.add("${entry.value} ${entry.key}")
            }
        }
        // Add pieces
        if (pieces > 0) {
            combinedMeasures.add("$pieces")
        }
        return combinedMeasures.joinToString { it }
    }
}