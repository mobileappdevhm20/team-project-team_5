package edu.hm.foodweek.inject

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.hm.foodweek.FoodWeekDatabase
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.screen.MealPlanViewModel
import edu.hm.foodweek.plans.screen.details.PlanDetailsViewModel
import edu.hm.foodweek.recipes.RecipeDetailViewModel
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.settings.screen.SettingsViewModel
import edu.hm.foodweek.users.persistence.UserRepository
import edu.hm.foodweek.util.DatabaseEntityCreator
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
        val userProvider = get<UserProvider>()
        Room.databaseBuilder(
            androidContext(),
            FoodWeekDatabase::class.java,
            "food_week_database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    DatabaseEntityCreator.insertPresetIntoDatabase(get(), get())
                    DatabaseEntityCreator.createUser(get(), userProvider.getUserID())
                }
            })
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
    viewModel { WeekViewModel(get(), get(), get(), androidApplication()) }
    single { MealPlanViewModel(get(), androidApplication()) }
    viewModel { SettingsViewModel(get(), get(), get(), androidApplication()) }

    // Amplify Backend
    single { FoodWeekClient() }
}