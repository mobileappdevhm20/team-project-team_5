package edu.hm.foodweek.shopping.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.recipes.persistence.model.IngredientAmount
import edu.hm.foodweek.users.persistence.UserRepository
import edu.hm.foodweek.util.extensions.mapSkipNulls

class ShoppingViewModel(
    val userRepository: UserRepository,
    val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    val ingredients: LiveData<List<IngredientAmount>?> = userRepository.getCurrentWeek()
        .switchMap { mealPlanRepository.getLiveDataMealPlanById(it) }
        .mapSkipNulls {
            it.meals.map { meal ->
                meal.recipe.ingredients
            }.flatten()
        }
}