package edu.hm.foodweek.plans.persistence.model

import kotlin.random.Random
import kotlin.random.nextInt

enum class WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    companion object {
        fun getRandom(seed: Long = 0L): WeekDay {
            return when (Random(seed).nextInt(IntRange(0, 6))) {
                0 -> MONDAY
                1 -> TUESDAY
                2 -> WEDNESDAY
                3 -> THURSDAY
                4 -> FRIDAY
                5 -> SATURDAY
                6 -> SUNDAY
                else -> MONDAY
            }
        }
    }
}

