package edu.hm.foodweek.util

import android.util.Log
import edu.hm.foodweek.plans.persistence.MealPlanDao
import edu.hm.foodweek.plans.persistence.RecipeDao
import edu.hm.foodweek.plans.persistence.model.Meal
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.model.Ingredient
import edu.hm.foodweek.recipes.persistence.model.IngredientAmount
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.users.persistence.UserDao
import edu.hm.foodweek.users.persistence.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

object DatabaseEntityCreator {
    // Insert data using daos
    var recipeId = 1L
    var mealPlanId = 1L

    val recipe1 = Recipe(
        recipeId = recipeId++,
        title = "Initial Recipe",
        description = "Initial description"
    )
    val recipe2 = Recipe(
        recipeId = recipeId++,
        title = "Tomato Dipp with Bread",
        description = "The best way to use old tomatoes",
        url = "https://www.gimmesomeoven.com/wp-content/uploads/2017/02/Catalan-Tomato-Bread-Pan-Con-Tomate-Recipe.jpg",
        ingredients = arrayListOf(
            IngredientAmount(Ingredient("tomato"), "3 pieces"),
            IngredientAmount(Ingredient("Bread"), "1kg")
        ),
        steps = arrayListOf(
            "munch those lucky tomatoes",
            "heat them in the microwave for 3 minutes",
            "cut bread into slices",
            "toast them",
            "dipp them in the tomato sausage",
            "enjoy"
        )
    )

    val recipe3 = Recipe(
        recipeId = recipeId++,
        title = "Nutella Toast",
        description = "toast and nutella",
        url = "https://www.einfachbacken.de/sites/einfachbacken.de/files/styles/facebook/public/2020-01/french_toast_mit_nutella.jpg?h=4521fff0&itok=YdSpSTsN",
        ingredients = arrayListOf(
            IngredientAmount(Ingredient("Nutella"), "50g"),
            IngredientAmount(Ingredient("Bread"), "1 piece")
        ),
        steps = arrayListOf(
            "take bread slice",
            "spread the nutella on the bread",
            "eat"
        )
    )

    val recipe4 = Recipe(
        recipeId = recipeId++,
        title = "cornflakes",
        description = "cornflakes with milk",
        url = "https://www.lidl-kochen.de/images/recipe/17367/cornflakes-mit-milch-133578.jpg",
        ingredients = arrayListOf(
            IngredientAmount(Ingredient("Cornflakes"), "90g"),
            IngredientAmount(Ingredient("Milk"), "100ml"),
            IngredientAmount(Ingredient("Fruits"), "50g")
        ),
        steps = arrayListOf(
            "optional: cut fruits",
            "put cornflakes and fruits in a bowl",
            "milk on it"
        )
    )

    val recipe5 = Recipe(
        recipeId = recipeId++,
        title = "Pizza",
        description = "Basic Pizza",
        url = "https://www.delonghi.com/Global/recipes/multifry/pizza_fresca.jpg",
        ingredients = arrayListOf(
            IngredientAmount(Ingredient("garlic powder"), "5g"),
            IngredientAmount(Ingredient("olive oil"), "10ml"),
            IngredientAmount(Ingredient("warm water"), "750ml"),
            IngredientAmount(Ingredient("Tomatoes"), "5 pieces"),
            IngredientAmount(Ingredient("cheese"), "500g"),
            IngredientAmount(Ingredient("herbs"), "10g")
        ),
        steps = arrayListOf(
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
        "https://www.gnjumc.org/content/uploads/2017/02/red-tomato-meteorite-1.jpg",
        "userId1",
        true,
        "username",
        listOf(
            Meal(WeekDay.MONDAY, MealTime.DINNER, recipe2)
        )
    )

    val mealplan2 = MealPlan(
        mealPlanId++,
        "tomato plan",
        "reuse all your tomatoes",
        "https://www.gnjumc.org/content/uploads/2017/02/red-tomato-meteorite-1.jpg",
        "userId2",
        true,
        "username",
        listOf(
            Meal(WeekDay.MONDAY, MealTime.DINNER, recipe2),
            Meal(WeekDay.TUESDAY, MealTime.DINNER, recipe2),
            Meal(WeekDay.WEDNESDAY, MealTime.DINNER, recipe2),
            Meal(WeekDay.THURSDAY, MealTime.LUNCH, recipe2),
            Meal(WeekDay.FRIDAY, MealTime.DINNER, recipe2),
            Meal(WeekDay.SATURDAY, MealTime.LUNCH, recipe3),
            Meal(WeekDay.SUNDAY, MealTime.DINNER, recipe2),
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5)
        )
    )

    val mealplan3 = MealPlan(
        mealPlanId++,
        "Pizza all day long",
        "this is for the pizza lovers",
        "https://www.gnjumc.org/content/uploads/2017/02/red-tomato-meteorite-1.jpg",
        "userId2",
        true,
        "username",
        listOf(
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5),
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5),
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5),
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5),
            Meal(WeekDay.getRandom(), MealTime.getRandom(), recipe5)
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
            // Recipes
            for (recipe in createRecipes()) {
                val recipeId = recipeDao.createRecipe(recipe)
                Log.i("DatabaseEntityCreator", "recipe ${recipe.title} ($recipeId) created")
            }
            // MealPlans
            for (mealPlan in createMealPlans()) {
                val mealPlanId = mealPlanDao.createMealPlan(mealPlan)
                Log.i("DatabaseEntityCreator", "mealplan ${mealPlan.planId} ($mealPlanId) created")
            }
        }.invokeOnCompletion {
            Log.i("DatabaseEntityCreator", "database insertion finished")
        }
    }

    val localUserWithMap = User(
        "null",
        "LocalUser",
        mapOf(Pair<Int, Long>(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR), mealplan2.planId))
    )
    val localUserWithoutMap = User("null", "LocalUser", emptyMap())

    fun createUser(userDao: UserDao, androidId: String) {
        val usedUser = localUserWithoutMap
        CoroutineScope(Dispatchers.IO).launch {
            usedUser.userId = androidId
            userDao.insert(usedUser)
        }.invokeOnCompletion {
            Log.i("DatabaseEntityCreator", "local user created :$usedUser")
        }
    }

}