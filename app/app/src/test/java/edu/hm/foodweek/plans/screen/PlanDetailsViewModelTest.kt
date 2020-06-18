package edu.hm.foodweek.plans.screen

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.hm.foodweek.inject.appModule
import io.mockk.MockKAnnotations
import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule

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
        stopKoin()
    }

    @Test
    fun getItems() {
//        val mockRecipeRepo = declareMock<RecipeRepository> {
//            coEvery { getRecipeById(any()) } returns recipe2
//        }
//        val mockMealPlanRepo = declareMock<MealPlanRepository> {
//            every { getLiveDataMealPlanById(any()) } returns MutableLiveData(createMealPlans().first())
//        }
//
//        runBlocking {
//            val viewModel: PlanDetailsViewModel = get(parameters = { parametersOf(0) })
//            val mealPlanItems = viewModel.items.getOrAwaitValue()
//            val want = listOf(
//                PlanDetailsItem(
//                    day = WeekDay.MONDAY,
//                    dishImageURL = "https://icons.iconarchive.com/icons/papirus-team/papirus-status/512/image-missing-icon.png",
//                    breakfastTitle = "-",
//                    lunchTitle = "-",
//                    dinnerTitle = "Tomato Dipp with Bread"
//                )
//            )
//            Assert.assertEquals("Generated meal plan items should be there", want, mealPlanItems)
//        }
//
//        verify(atLeast = 1) { mockMealPlanRepo.getLiveDataMealPlanById(0) }
//        coVerify(atLeast = 1) { mockRecipeRepo.getRecipeById(any()) }
    }
}