package edu.hm.foodweek.util

import android.content.Context
import androidx.fragment.app.Fragment
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.week.screen.WeekViewModel
import edu.hm.foodweek.week.screen.WeekViewModelFactory

object InjectorUtils {

    private var weekViewModel: WeekViewModel? = null

    private fun getRecipeRepository(context: Context): RecipeRepository {
        return RecipeRepository.getInstance(context)
    }

    private fun getMealPlanRepository(context: Context): MealPlanRepository {
        return MealPlanRepository.getInstance(context)
    }

    private fun provideWeekViewModelFactory(fragment: Fragment): WeekViewModelFactory {
        val repository = getMealPlanRepository(fragment.requireContext())
        return WeekViewModelFactory(repository, fragment.requireActivity().application)
    }

    fun provideWeekViewModel(fragment: Fragment): WeekViewModel {
        if (weekViewModel == null) {
            weekViewModel = provideWeekViewModelFactory(fragment).create(WeekViewModel::class.java)
        }
        return weekViewModel!!
    }


}