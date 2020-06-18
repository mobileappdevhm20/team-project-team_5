package edu.hm.foodweek.plans.screen

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.hm.foodweek.inject.appModule
import io.mockk.MockKAnnotations
import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule

class MealPlanViewModelTest : KoinTest, Application() {

    @Rule
    @JvmField
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        androidContext(this@MealPlanViewModelTest)
        modules(appModule)
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(type = clazz, relaxed = true)
    }

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
        stopKoin()
    }

    /*
    @Test
    fun getItems() {
        val mockMealPlanRepository = declareMock<MealPlanRepository> {
            every { getLiveDataAllMealPlans(any()) } returns MutableLiveData(
                createMealPlans()
            )
            every { getOwnMealPlans() } returns Observable.just(listOf(mealplan2, mealplan3))
        }

        runBlocking {
            val viewModel: MealPlanViewModel = get()
            val allMealPlans = viewModel.browsablePlans.getOrAwaitValue()
            val ownMealPlans = viewModel.managedPlans.getOrAwaitValue()

            val expected = createMealPlans().map { BrowseableMealPlan(it) }

            Thread.sleep(500)
            assertEquals("All mealPlans should be there", expected, allMealPlans)
            assertEquals(
                "Own mealPlans should be there",
                listOf(mealplan2, mealplan3),
                ownMealPlans
            )
        }

        verify(atLeast = 1) { mockMealPlanRepository.getLiveDataAllMealPlans(any()) }
        verify(atLeast = 1) { mockMealPlanRepository.getOwnMealPlans() }
    }*/
}