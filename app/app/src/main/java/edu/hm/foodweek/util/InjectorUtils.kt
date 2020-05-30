package edu.hm.foodweek.util

import android.content.Context
import androidx.fragment.app.Fragment
import edu.hm.foodweek.developersettings.SettingsViewModelFactory
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.screen.MealPlanViewModel
import edu.hm.foodweek.plans.screen.MealPlanViewModelFactory
import edu.hm.foodweek.plans.screen.PlanDetailsViewModelFactory
import edu.hm.foodweek.recipes.RecipeDetailViewModel
import edu.hm.foodweek.recipes.RecipeDetailViewModelFactory
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.settings.screen.SettingsViewModel
import edu.hm.foodweek.week.screen.WeekViewModel
import edu.hm.foodweek.week.screen.WeekViewModelFactory

object InjectorUtils {

    private var weekViewModel: WeekViewModel? = null
    private var recipeDetailViewmodel: RecipeDetailViewModel? = null
    private var settingsViewModel: SettingsViewModel? = null
    private var mealPlanViewModel: MealPlanViewModel? = null


    private fun getRecipeRepository(context: Context): RecipeRepository {
        return RecipeRepository.getInstance(context)
    }

    private fun getMealPlanRepository(context: Context): MealPlanRepository {
        return MealPlanRepository.getInstance(context)
    }

    private fun provideWeekViewModelFactory(fragment: Fragment): WeekViewModelFactory {
        val mealPlanRepository = getMealPlanRepository(fragment.requireContext())
        val recipeRepository = getRecipeRepository(fragment.requireContext())
        return WeekViewModelFactory(
            mealPlanRepository,
            recipeRepository,
            fragment.requireActivity().application
        )
    }

    fun providePlanDetailsViewModelFactory(
        fragment: Fragment,
        mealPlanId: Long
    ): PlanDetailsViewModelFactory {
        val mealPlanRepository = getMealPlanRepository(fragment.requireContext())
        val recipeRepository = getRecipeRepository(fragment.requireContext())
        return PlanDetailsViewModelFactory(
            mealPlanId,
            mealPlanRepository,
            recipeRepository,
            fragment.requireActivity().application
        )
    }

    private fun provideSettingsViewModelFactory(fragment: Fragment): SettingsViewModelFactory {
        val mealPlanRepository = getMealPlanRepository(fragment.requireContext())
        val recipeRepository = getRecipeRepository(fragment.requireContext())
        return SettingsViewModelFactory(
            mealPlanRepository,
            recipeRepository,
            fragment.requireActivity().application
        )
    }

    private fun provideRecipeDetailViewModelFactory(fragment: Fragment): RecipeDetailViewModelFactory {
        val recipeRepository = getRecipeRepository(fragment.requireContext())
        return RecipeDetailViewModelFactory(
            recipeRepository,
            fragment.requireActivity().application
        )
    }

    fun provideWeekViewModel(fragment: Fragment): WeekViewModel {
        if (weekViewModel == null) {
            weekViewModel = provideWeekViewModelFactory(fragment).create(WeekViewModel::class.java)
        }
        return weekViewModel!!
    }

    fun provideSettingsViewModel(fragment: Fragment): SettingsViewModel {
        if (settingsViewModel == null) {
            settingsViewModel =
                provideSettingsViewModelFactory(fragment).create(SettingsViewModel::class.java)
        }
        return settingsViewModel!!
    }

    fun provideRecipeDetailViewModel(fragment: Fragment): RecipeDetailViewModel {
        if (recipeDetailViewmodel == null) {
            recipeDetailViewmodel = provideRecipeDetailViewModelFactory(fragment).create(RecipeDetailViewModel::class.java)
        }
        return recipeDetailViewmodel!!
    }

    private fun provideMealPlanViewModelFactory(fragment: Fragment): MealPlanViewModelFactory {
        val mealPlanRepository = getMealPlanRepository(fragment.requireContext())
        return MealPlanViewModelFactory(
            mealPlanRepository,
            fragment.requireActivity().application
        )
    }

    fun provideMealPlanViewModel(fragment: Fragment): MealPlanViewModel {
        if (mealPlanViewModel == null) {
            mealPlanViewModel =
                provideMealPlanViewModelFactory(fragment).create(MealPlanViewModel::class.java)
        }
        return mealPlanViewModel!!
    }

}