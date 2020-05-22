package edu.hm.foodweek

import android.content.Context
import androidx.room.*
import edu.hm.foodweek.plans.persistence.MealPlanDao
import edu.hm.foodweek.plans.persistence.model.*
import edu.hm.foodweek.recipes.Recipe
import edu.hm.foodweek.storage.User
import edu.hm.foodweek.storage.UserDao

@Database(
    entities = [User::class, MealPlan::class, Recipe::class, Meal::class, MealRecipeCrossRef::class],
    version = 1
)
@TypeConverters(Converters::class)
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

class Converters {
    @TypeConverter
    fun toWeekDay(value: String) = enumValueOf<WeekDay>(value)

    @TypeConverter
    fun fromWeekDay(value: WeekDay) = value.name

    @TypeConverter
    fun toMealTime(value: String) = enumValueOf<MealTime>(value)

    @TypeConverter
    fun fromMealTime(value: MealTime) = value.name
}
