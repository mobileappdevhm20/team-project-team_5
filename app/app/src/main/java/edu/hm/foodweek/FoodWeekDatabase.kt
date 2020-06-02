package edu.hm.foodweek

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.hm.foodweek.plans.persistence.MealPlanDao
import edu.hm.foodweek.plans.persistence.RecipeDao
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.users.persistence.UserDao
import edu.hm.foodweek.users.persistence.model.User
import edu.hm.foodweek.util.Converters


@Database(
    entities = [MealPlan::class, User::class, Recipe::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class FoodWeekDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun recipeDao(): RecipeDao
}

