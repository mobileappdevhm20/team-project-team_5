package edu.hm.foodweek.inject

import androidx.room.Room
import edu.hm.foodweek.FoodWeekDatabase
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.screen.MealPlanViewModel
import edu.hm.foodweek.plans.screen.create.CreateMealPlanViewModel
import edu.hm.foodweek.plans.screen.details.PlanDetailsViewModel
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.recipes.screen.detail.RecipeDetailViewModel
import edu.hm.foodweek.shopping.screen.ShoppingViewModel
import edu.hm.foodweek.users.persistence.UserRepository
import edu.hm.foodweek.util.UserProvider
import edu.hm.foodweek.util.amplify.FoodWeekClient
import edu.hm.foodweek.week.screen.WeekViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Room database
    single {
        Room.databaseBuilder(
            androidContext(),
            FoodWeekDatabase::class.java,
            "food_week_database"
        )
            .fallbackToDestructiveMigration().build()
    }

    // User-ID provider
    single { UserProvider(androidContext()) }

    // DAOs
    single { get<FoodWeekDatabase>().userDao() }
    single { get<FoodWeekDatabase>().mealPlanDao() }
    single { get<FoodWeekDatabase>().recipeDao() }

    // Repositories
    single { MealPlanRepository(get(), get(), get()) }
    single { RecipeRepository(get(), get()) }
    single { UserRepository(get()) }

    // ViewModels
    viewModel { (id: Long) ->
        PlanDetailsViewModel(
            id,
            get(),
            androidApplication()
        )
    }
    viewModel { (id: Long) ->
        RecipeDetailViewModel(
            id,
            get(),
            androidApplication()
        )
    }
    viewModel { WeekViewModel(get(), get(), androidApplication()) }
    single { MealPlanViewModel(get(), androidApplication()) }
    viewModel { (mealplanid: Long) ->
        CreateMealPlanViewModel(
            mealplanid,
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
    viewModel { ShoppingViewModel(get(), get(), get()) }
    // Amplify Backend
    single { FoodWeekClient() }
}