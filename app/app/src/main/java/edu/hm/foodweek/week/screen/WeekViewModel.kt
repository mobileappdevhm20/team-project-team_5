package edu.hm.foodweek.week.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.users.persistence.UserRepository
import edu.hm.foodweek.util.extensions.combineLatest
import edu.hm.foodweek.util.extensions.map
import edu.hm.foodweek.util.extensions.mapSkipNulls
import org.koin.android.logger.AndroidLogger
import java.util.*

class WeekViewModel(
    private val mealPlanRepository: MealPlanRepository,
    userRepository: UserRepository,
    application: Application
) :
    AndroidViewModel(
        application
    ) {
    // first fetch the current Week id
    private val currentMealPlanId = userRepository.getCurrentWeek().mapSkipNulls {
        AndroidLogger().debug("$it")
        it
    }

    // check if there is a mealplan for this week
    val isMealPlanForCurrentWeekSet = currentMealPlanId.map { it != -1L }

    // load the mealplan
    private val mealPlan = currentMealPlanId.switchMap {
        if (it != -1L) {
            mealPlanRepository.getLiveDataMealPlanById(it)
        } else {
            // provide Default Mealplan, the values wont be used
            liveData { emit(MealPlan(0, "", "", "", "nouser", true, creatorUsername = "username")) }
        }
    }
    val planId = mealPlan.mapSkipNulls { it.planId }
    val planTitle = mealPlan
        .mapSkipNulls { it.title }
        .combineLatest(isMealPlanForCurrentWeekSet)
        .mapSkipNulls { pair ->
            if (pair.second == null || pair.second == true) {
                return@mapSkipNulls pair.first
            } else {
                return@mapSkipNulls "No MealPlan assigned for this Week"
            }
        }
    val planDescription = mealPlan.mapSkipNulls { it.description }
    val planUrl = mealPlan.mapSkipNulls { it.imageURL }

    val meals = mealPlan
        .map { mealplan ->
            val today = Calendar.getInstance()
            mealplan?.meals?.filter { meal ->
                val mealDay = Calendar.getInstance()
                mealDay.set(Calendar.DAY_OF_WEEK, meal.day.asJavaCalendar())
                mealDay.after(today)
            }
        }
}