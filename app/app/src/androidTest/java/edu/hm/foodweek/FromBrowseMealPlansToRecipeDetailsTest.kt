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
class FromBrowseMealPlansToRecipeDetailsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
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

        val appCompatImageView = onView(
            allOf(
                withId(R.id.recipe_plan_picture), withContentDescription("Main Image of Recipe"),
                childAtPosition(
                    allOf(
                        withId(R.id.browse_plans_view_holder_linear_layout_1),
                        childAtPosition(
                            withId(R.id.browse_plans_recyclerView),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())
        Thread.sleep(emulatorSleepLongMilli)

        // Click to day details
        onView(withId(R.id.plan_details_recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        clickOnViewChild(R.id.plan_details_view_holder_imageView)
                    )
            )
        Thread.sleep(emulatorSleepLongMilli)

        // Click to recipe details
        onView(withId(R.id.plan_day_details_recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        clickOnViewChild(R.id.plan_day_details_recipe_view_holder_imageView)
                    )
            )
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
