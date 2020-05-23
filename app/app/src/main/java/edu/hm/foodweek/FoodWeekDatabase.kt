package edu.hm.foodweek

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.hm.foodweek.plans.persistence.MealPlanDao
import edu.hm.foodweek.plans.persistence.model.*
import edu.hm.foodweek.recipes.persistence.model.Ingredient
import edu.hm.foodweek.recipes.persistence.model.Recipe
import edu.hm.foodweek.users.persistence.UserDao
import edu.hm.foodweek.users.persistence.model.User

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
    // ################# WeekDay
    @TypeConverter
    fun toWeekDay(value: String) = enumValueOf<WeekDay>(value)

    @TypeConverter
    fun fromWeekDay(value: WeekDay) = value.name

    // ################# MealTime
    @TypeConverter
    fun toMealTime(value: String) = enumValueOf<MealTime>(value)

    @TypeConverter
    fun fromMealTime(value: MealTime) = value.name

    // ################# List<Ingredient>
    @TypeConverter
    fun toIngredientList(value: String) = run {
        val itemType = object : TypeToken<List<Ingredient>>() {}.type
        Gson().fromJson<List<Ingredient>>(value, itemType)
    }

    @TypeConverter
    fun fromIngredientList(value: List<Ingredient>) = Gson().toJson(value)

    // ################# List<String>
    @TypeConverter
    fun toStringList(value: String) = run {
        val itemType = object : TypeToken<List<String>>() {}.type
        Gson().fromJson<List<String>>(value, itemType)
    }

    @TypeConverter
    fun fromStringList(value: List<String>) = Gson().toJson(value)

    // ################# Set<String>
    @TypeConverter
    fun toStringSet(value: String) = run {
        val itemType = object : TypeToken<List<String>>() {}.type
        Gson().fromJson<List<String>>(value, itemType).toSet()
    }

    @TypeConverter
    fun fromStringSet(value: Set<String>) = Gson().toJson(value.toList())
}

