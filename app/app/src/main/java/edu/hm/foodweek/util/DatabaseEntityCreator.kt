package edu.hm.foodweek.util

import android.util.Log
import edu.hm.foodweek.plans.persistence.MealPlanDao
import edu.hm.foodweek.plans.persistence.RecipeDao
import edu.hm.foodweek.plans.persistence.model.Meal
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.model.Ingredient
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.recipes.persistence.model.UnitScale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseEntityCreator {
    // Insert data using daos
    var recipeId = 1L
    var mealPlanId = 1L

    val recipe1 = Recipe(
        recipeId++,
        "Initial Recipe",
        "Initial description"
    )
    val recipe2 = Recipe(
        recipeId++,
        "Tomato Dipp with Bread",
        "The best way to use old tomatoes",
        arrayListOf(
            Ingredient("tomato", edu.hm.foodweek.recipes.persistence.model.Unit(3, UnitScale.Pieces)),
            Ingredient("Bread", edu.hm.foodweek.recipes.persistence.model.Unit(1, UnitScale.KiloGram))
        ),
        arrayListOf(
            "munch those lucky tomatoes",
            "heat them in the microwave for 3 minutes",
            "cut bread into slices",
            "toast them",
            "dipp them in the tomato sausage",
            "enjoy"
        )
    )

    val recipe3 = Recipe(
        recipeId++,
        "Nutella Toast",
        "toast and nutella",
        arrayListOf(
            Ingredient("Nutella", edu.hm.foodweek.recipes.persistence.model.Unit(50, UnitScale.Gram)),
            Ingredient("Bread", edu.hm.foodweek.recipes.persistence.model.Unit(1, UnitScale.Pieces))
        ),
        arrayListOf(
            "take bread slice",
            "spread the nutella on the bread",
            "eat"
        )
    )

    val recipe4 = Recipe(
        recipeId++,
        "cornflakes",
        "cornflakes with milk",
        arrayListOf(
            Ingredient("Cornflakes", edu.hm.foodweek.recipes.persistence.model.Unit(90, UnitScale.Gram)),
            Ingredient("Milk", edu.hm.foodweek.recipes.persistence.model.Unit(100, UnitScale.Millilitre)),
            Ingredient("Fruits", edu.hm.foodweek.recipes.persistence.model.Unit(50, UnitScale.Gram))
        ),
        arrayListOf(
            "optional: cut fruits",
            "put cornflakes and fruits in a bowl",
            "milk on it"
        )
    )

    val recipe5 = Recipe(
        recipeId++,
        "Pizza",
        "Basic Pizza",
        arrayListOf(
            Ingredient("flour", edu.hm.foodweek.recipes.persistence.model.Unit(150, UnitScale.Gram)),
            Ingredient("yeast", edu.hm.foodweek.recipes.persistence.model.Unit(20, UnitScale.Millilitre)),
            Ingredient("sugar", edu.hm.foodweek.recipes.persistence.model.Unit(20, UnitScale.Gram)),
            Ingredient("salt", edu.hm.foodweek.recipes.persistence.model.Unit(10, UnitScale.Gram)),
            Ingredient("garlic powder", edu.hm.foodweek.recipes.persistence.model.Unit(5, UnitScale.Gram)),
            Ingredient("olive oil", edu.hm.foodweek.recipes.persistence.model.Unit(10, UnitScale.Millilitre)),
            Ingredient("warm water", edu.hm.foodweek.recipes.persistence.model.Unit(750, UnitScale.Millilitre)),
            Ingredient("Tomatoes", edu.hm.foodweek.recipes.persistence.model.Unit(5, UnitScale.Pieces)),
            Ingredient("cheese", edu.hm.foodweek.recipes.persistence.model.Unit(500, UnitScale.Gram)),
            Ingredient("herbs", edu.hm.foodweek.recipes.persistence.model.Unit(10, UnitScale.Gram))
        ),
        arrayListOf(
            "Dough: Take a bowl and put in the flour,yeast,sugar,salt,olive oil,water. Mix it for 10 Minutes",
            "Put a towel on the bowl and let the dough rest for 1h",
            "Sauce: mix tomatoes with herbs and garlic powder for 1 minute",
            "Roll out the Dough: To roll out the Dough, put a baking paper on the sheet. Optional you can use a Pizza stone if you have. " +
                    "Make sure the dough has a equally distributed thickness. put the tomatoe sauce equally on the pizza",
            "put the cheese on the pizza",
            "add topics on your own wishes"
        )
    )

    val mealplan1 = MealPlan(
        mealPlanId++,
        "Inital Title",
        "Inital description",
        "UTL",
        1,
        true,
        listOf(recipe1).map { recipe -> Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe.recipeId) }
    )

    val mealplan2 = MealPlan(
        mealPlanId++,
        "tomato plan",
        "reuse all your tomatoes",
        "www.gnjumc.org/content/uploads/2017/02/red-tomato-meteorite-1.jpg",
        0,
        true,
        listOf(
            Meal(WeekDay.MONDAY, MealTime.DINNER, recipe2.recipeId),
            Meal(WeekDay.WEDNESDAY, MealTime.DINNER, recipe2.recipeId),
            Meal(WeekDay.FRIDAY, MealTime.DINNER, recipe2.recipeId),
            Meal(WeekDay.SUNDAY, MealTime.DINNER, recipe2.recipeId),
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5.recipeId)
        )
    )

    val mealplan3 = MealPlan(
        mealPlanId++,
        "Pizza all day long",
        "this is for the pizza lovers",
        "www.gnjumc.org/content/uploads/2017/02/red-tomato-meteorite-1.jpg",
        0,
        true,
        listOf(
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5.recipeId),
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5.recipeId),
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5.recipeId),
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5.recipeId),
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5.recipeId)
        )
    )

    fun createRecipes(): List<Recipe> {
        val recipes = listOf(recipe1, recipe2, recipe3, recipe4, recipe5)
        Log.i("DatabaseEntityCreator", "recipes to create: $recipes")
        return recipes
    }


    fun createMealPlans(): List<MealPlan> {
        return listOf(mealplan1, mealplan2, mealplan3)
    }

    fun insertPresetIntoDatabase(recipeDao: RecipeDao, mealPlanDao: MealPlanDao) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("FoodWeekDatabase", "Insert recipe")
            for (recipe in createRecipes()) {
                val recipeId = recipeDao.createRecipe(recipe)
                Log.i("DatabaseEntityCreator", "recipe ${recipe.title} ($recipeId) created")
            }
            for (mealPlan in createMealPlans()) {
                val mealPlanId = mealPlanDao.createMealPlan(mealPlan)
                Log.i("DatabaseEntityCreator", "mealplan ${mealPlan.planId} ($mealPlanId) created")
            }
        }.invokeOnCompletion {
            Log.i("DatabaseEntityCreator", "database insertion finished")
        }
    }


}