package edu.hm.foodweek

import android.content.Context
import androidx.room.*
import edu.hm.foodweek.plans.persistence.MealPlanDao
import edu.hm.foodweek.plans.persistence.RecipeDao
import edu.hm.foodweek.plans.persistence.model.*
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.users.persistence.UserDao
import edu.hm.foodweek.users.persistence.model.User
import edu.hm.foodweek.util.Converters

@Database(
    entities = [MealPlan::class,User::class,Recipe::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class FoodWeekDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: FoodWeekDatabase? = null
        fun getInstance(context: Context): FoodWeekDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FoodWeekDatabase::class.java,
                        "food_week_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}

