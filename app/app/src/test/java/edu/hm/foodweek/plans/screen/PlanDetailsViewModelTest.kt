package edu.hm.foodweek.plans.screen

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import edu.hm.foodweek.getOrAwaitValue
import edu.hm.foodweek.inject.appModule
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.plans.screen.details.PlanDetailsItem
import edu.hm.foodweek.plans.screen.details.PlanDetailsViewModel
import edu.hm.foodweek.recipes.persistence.RecipeRepository
import edu.hm.foodweek.util.DatabaseEntityCreator.createMealPlans
import edu.hm.foodweek.util.DatabaseEntityCreator.recipe2
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.logger.Level
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

class PlanDetailsViewModelTest : KoinTest, Application() {

    @Rule
    @JvmField
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        androidContext(this@PlanDetailsViewModelTest)
        modules(appModule)
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(type = clazz, relaxed = true)
    }

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun getItems() {
        val mockRecipeRepo = declareMock<RecipeRepository> {
            coEvery { getRecipeById(any()) } returns recipe2
        }
        val mockMealPlanRepo = declareMock<MealPlanRepository> {
            every { getLiveDataMealPlanById(any()) } returns MutableLiveData(createMealPlans().first())
        }

        runBlocking {
            val viewModel: PlanDetailsViewModel = get(parameters = { parametersOf(0) })
            val mealPlanItems = viewModel.items.getOrAwaitValue()
            val want = listOf(
                PlanDetailsItem(
                    day = WeekDay.MONDAY,
                    dishImageURL = "https://icons.iconarchive.com/icons/papirus-team/papirus-status/512/image-missing-icon.png",
                    breakfastTitle = "-",
                    lunchTitle = "-",
                    dinnerTitle = "Tomato Dipp with Bread"
                )
            )
            Assert.assertEquals("Generated meal plan items should be there", want, mealPlanItems)
        }

        verify(atLeast = 1) { mockMealPlanRepo.getLiveDataMealPlanById(0) }
        coVerify(atLeast = 1) { mockRecipeRepo.getRecipeById(any()) }
    }
}