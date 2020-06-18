package edu.hm.foodweek


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SubscribeUnsubscribeTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun subscribeUnsubscribeTest() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_plan), withContentDescription("Meal Plans"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())
        Thread.sleep(emulatorSleepLongMilli)

        // Subscribe
        onView(withId(R.id.browse_plans_recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        clickOnViewChild(R.id.subscribe_button)
                    )
            )
        Thread.sleep(emulatorSleepShortMilli)

        // Unsubscribe
        onView(withId(R.id.browse_plans_recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        clickOnViewChild(R.id.subscribe_button)
                    )
            )
        Thread.sleep(emulatorSleepShortMilli)

        // Subscribe again
        onView(withId(R.id.browse_plans_recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        clickOnViewChild(R.id.subscribe_button)
                    )
            )
        Thread.sleep(emulatorSleepShortMilli)

        val tabView = onView(
            allOf(
                withContentDescription("Manage"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.plan_tab_layout),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(click())
        Thread.sleep(emulatorSleepLongMilli)

        // Unsubscribe
        onView(withId(R.id.manage_plans_recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        clickOnViewChild(R.id.button_unsubscribe)
                    )
            )
        Thread.sleep(emulatorSleepShortMilli)
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
