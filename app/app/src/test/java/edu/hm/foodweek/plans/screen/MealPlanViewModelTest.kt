package edu.hm.foodweek.plans.screen

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import edu.hm.foodweek.getOrAwaitValue
import edu.hm.foodweek.inject.appModule
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.util.DatabaseEntityCreator.createMealPlans
import edu.hm.foodweek.util.DatabaseEntityCreator.mealplan2
import edu.hm.foodweek.util.DatabaseEntityCreator.mealplan3
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

class MealPlanViewModelTest : KoinTest, Application() {

    @Rule
    @JvmField
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var observer: Observer<List<PlanTimelineItem>>

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
        val mockMealPlanRepository = declareMock<MealPlanRepository> {
            every { getLiveDataAllMealPlans() } returns MutableLiveData(
                createMealPlans()
            )
            every { getMealPlanCreatedByUser(any()) } returns MutableLiveData(
                listOf(mealplan2, mealplan3)
            )
        }

        runBlocking {
            val viewModel: MealPlanViewModel = get()
            val allMealPlans = viewModel.allMealPlans.getOrAwaitValue()
            val ownMealPlans = viewModel.allMealPlansCreatedByUser.getOrAwaitValue()

            assertEquals("All mealPlans should be there", createMealPlans(), allMealPlans)
            assertEquals(
                "Own mealPlans should be there",
                listOf(mealplan2, mealplan3),
                ownMealPlans
            )
        }

        verify(atLeast = 1) { mockMealPlanRepository.getLiveDataAllMealPlans() }
        verify(atLeast = 1) { mockMealPlanRepository.getMealPlanCreatedByUser(0) }
    }
}