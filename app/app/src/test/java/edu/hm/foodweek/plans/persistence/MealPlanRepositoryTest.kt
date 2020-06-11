package edu.hm.foodweek.plans.persistence

import android.app.Application
import androidx.lifecycle.MutableLiveData
import edu.hm.foodweek.inject.appModule
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.util.DatabaseEntityCreator
import edu.hm.foodweek.util.DatabaseEntityCreator.createMealPlans
import edu.hm.foodweek.util.DatabaseEntityCreator.mealplan1
import edu.hm.foodweek.util.DatabaseEntityCreator.mealplan2
import edu.hm.foodweek.util.DatabaseEntityCreator.mealplan3
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

class MealPlanRepositoryTest : KoinTest, Application() {

    lateinit var mockMealPlanDao: MealPlanDao
    lateinit var mealPlanRepository: MealPlanRepository
    private var userId = DatabaseEntityCreator.mealplan2.creatorId
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        androidContext(this@MealPlanRepositoryTest)
        modules(appModule)
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(type = clazz, relaxed = true)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockMealPlanDao = declareMock {
            every { getAllMealPlans() } returns MutableLiveData(
                createMealPlans()
            )
            every { getMealPlan(1) } returns MutableLiveData(mealplan1)
            every { getMealPlanCreatedByUser(userId) } returns MutableLiveData(
                listOf(mealplan2, mealplan3)
            )
            coJustRun { createMealPlan(any()) }
        }
        mealPlanRepository = MealPlanRepository(mockMealPlanDao)
    }

    @Test
    fun testGetLiveDataAllMealPlans() {
        val expected = createMealPlans()
        val actual = mealPlanRepository.getLiveDataAllMealPlans().value

        verify(atLeast = 1) { mockMealPlanDao.getAllMealPlans() }
        testEqualityOfMealPlans(expected, actual)
    }


    @Test
    fun testGetLiveDataMealPlanById() {
        val expected = mealplan1
        val actual = mealPlanRepository.getLiveDataMealPlanById(1).value

        verify(atLeast = 1) { mockMealPlanDao.getMealPlan(1) }
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testGetMealPlanCreatedByUser() {
        val expected = listOf(mealplan2, mealplan3)
        val actual = mealPlanRepository.getMealPlanCreatedByUser(userId).value

        verify(atLeast = 1) { mockMealPlanDao.getMealPlanCreatedByUser(userId) }
        testEqualityOfMealPlans(expected, actual)
    }

    @Test
    fun testCreateMealPlan() {
        runBlocking {
            mealPlanRepository.createMealPlan(mealplan1)
        }

        coVerify(atLeast = 1) { mockMealPlanDao.createMealPlan(mealplan1) }
    }

    private fun testEqualityOfMealPlans(
        expected: List<MealPlan>,
        actual: List<MealPlan>?
    ) {
        val expectedIterator = expected.iterator()
        val actualIterator = actual!!.iterator()

        while (expectedIterator.hasNext() && actualIterator.hasNext()) {
            val currentExpected = expectedIterator.next()
            val currentActual = actualIterator.next()
            if (currentActual != currentExpected) {
                Assert.assertTrue(false)
            }
        }

        Assert.assertTrue(true)
    }
}