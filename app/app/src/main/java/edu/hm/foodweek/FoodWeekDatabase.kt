package edu.hm.foodweek

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.hm.foodweek.plans.MealPlan
import edu.hm.foodweek.plans.persistence.MealPlanDao
import edu.hm.foodweek.storage.User
import edu.hm.foodweek.storage.UserDao

@Database(entities = [User::class, MealPlan::class], version = 1)
abstract class FoodWeekDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealPlanDao(): MealPlanDao

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
