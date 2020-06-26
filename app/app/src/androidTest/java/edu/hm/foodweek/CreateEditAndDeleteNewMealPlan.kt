package edu.hm.foodweek


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateEditAndDeleteNewMealPlan {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun createEditAndDeleteNewMealPlan() {
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
        Thread.sleep(emulatorSleepShortMilli)

        val floatingActionButton = onView(
            allOf(
                withId(R.id.add_plan_fab),
                childAtPosition(
                    allOf(
                        withId(R.id.manage_plans_view_holder_relative_layout),
                        withParent(withId(R.id.plan_view_pager))
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())
        Thread.sleep(emulatorSleepShortMilli)

        val appCompatButton = onView(
            allOf(
                withId(R.id.btn_enter), withText("Add Recipe"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())
        Thread.sleep(emulatorSleepLongMilli)

        val recyclerView = onView(
            allOf(
                withId(R.id.add_recipe_dialog_recylcerView),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(emulatorSleepShortMilli)

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.btn_add_selected_recipe), withText("Add Recipe"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        val appCompatRadioButton = onView(
            allOf(
                withId(R.id.bTuesday), withText("Tuesday"),
                childAtPosition(
                    allOf(
                        withId(R.id.radioG1),
                        childAtPosition(
                            withClassName(`is`("android.widget.HorizontalScrollView")),
                            0
                        )
                    ),
                    1
                )
            )
        )
        appCompatRadioButton.perform(scrollTo(), click())

        val appCompatRadioButton2 = onView(
            allOf(
                withId(R.id.imageView2), withText("Lunch"),
                childAtPosition(
                    allOf(
                        withId(R.id.radioG2),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            1
                        )
                    ),
                    1
                )
            )
        )
        appCompatRadioButton2.perform(scrollTo(), click())

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.btn_enter), withText("Add Recipe"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        appCompatButton3.perform(scrollTo(), click())
        Thread.sleep(emulatorSleepLongMilli)

        val recyclerView2 = onView(
            allOf(
                withId(R.id.add_recipe_dialog_recylcerView),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    1
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(4, click()))

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.btn_add_selected_recipe), withText("Add Recipe"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        val floatingActionButton2 = onView(
            allOf(
                withId(R.id.floatingActionButton),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(click())
        Thread.sleep(emulatorSleepShortMilli)

        val appCompatEditText = onView(
            allOf(
                withId(R.id.submit_dialog_text_view_title),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("Draft Espresso"), closeSoftKeyboard())
        Thread.sleep(emulatorSleepShortMilli)

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.submit_dialog_text_view_description),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("Draft"), closeSoftKeyboard())
        Thread.sleep(emulatorSleepShortMilli)

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.submit_dialog_btn_draft), withText("Draft"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        3
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())
        Thread.sleep(emulatorSleepShortMilli)

        onView(withId(R.id.manage_plans_recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        clickOnViewChild(R.id.button_edit_plan)
                    )
            )
        Thread.sleep(emulatorSleepShortMilli)

        val floatingActionButton3 = onView(
            allOf(
                withId(R.id.floatingActionButton),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        floatingActionButton3.perform(click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.submit_dialog_text_view_description), withText("Draft"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("Draft Updated"))
        Thread.sleep(emulatorSleepShortMilli)

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.submit_dialog_text_view_description), withText("Draft Updated"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(closeSoftKeyboard())
        Thread.sleep(emulatorSleepShortMilli)

        val appCompatButton6 = onView(
            allOf(
                withId(R.id.submit_dialog_btn_draft), withText("Draft"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        3
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton6.perform(click())
        Thread.sleep(emulatorSleepShortMilli)

        onView(withId(R.id.manage_plans_recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        clickOnViewChild(R.id.button_delete_plan)
                    )
            )

        val appCompatButton7 = onView(
            allOf(
                withId(android.R.id.button1), withText("Yes"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.buttonPanel),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton7.perform(scrollTo(), click())
        Thread.sleep(emulatorSleepLongMilli)


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
