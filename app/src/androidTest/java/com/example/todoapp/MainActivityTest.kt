package com.example.todoapp


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val appCompatCheckBox = onView(
            allOf(
                withId(R.id.complete_checkbox),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tasks_list),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatCheckBox.perform(click())

        val recyclerView = onView(
            allOf(
                withId(R.id.tasks_list),
                childAtPosition(
                    withId(R.id.tasks_linear_layout),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val recyclerView2 = onView(
            allOf(
                withId(R.id.tasks_list),
                childAtPosition(
                    withId(R.id.tasks_linear_layout),
                    1
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val floatingActionButton = onView(
            allOf(
                withId(R.id.edit_task_fab),
                childAtPosition(
                    allOf(
                        withId(R.id.coordinator_layout),
                        childAtPosition(
                            withId(R.id.nav_host),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.add_task_description_edit_text),
                withText("Ground looks good, no foundation work required."),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        )
        appCompatEditText.perform(
            scrollTo(),
            replaceText("Ground looks good, no foundation work required. viola")
        )

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.add_task_description_edit_text),
                withText("Ground looks good, no foundation work required. viola"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(closeSoftKeyboard())

        val floatingActionButton2 = onView(
            allOf(
                withId(R.id.save_task_fab),
                childAtPosition(
                    allOf(
                        withId(R.id.coordinator_layout),
                        childAtPosition(
                            withId(R.id.nav_host),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(click())
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
