package edu.hm.foodweek.plans.persistence.model

import kotlin.random.Random
import kotlin.random.nextInt

enum class MealTime {
    BREAKFAST, LUNCH, DINNER;

    companion object {
        fun getRandom(): MealTime {
            return when (Random.nextInt(IntRange(0, 2))) {
                0 -> BREAKFAST
                1 -> LUNCH
                2 -> DINNER
                else -> BREAKFAST
            }
        }

    }
}