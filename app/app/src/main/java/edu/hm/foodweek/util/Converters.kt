package edu.hm.foodweek.util

import androidx.room.TypeConverter
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.model.Ingredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.hm.foodweek.plans.persistence.model.Meal


class Converters {
    @TypeConverter
    fun fromMeals(value: List<Meal>) = Gson().toJson(value)

    @TypeConverter
    fun toMeals(value: String) = run {
        val itemType = object : TypeToken<List<Meal>>() {}.type
        Gson().fromJson<List<Meal>>(value, itemType)
    }

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