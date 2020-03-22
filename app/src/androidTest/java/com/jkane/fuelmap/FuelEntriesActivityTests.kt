package com.jkane.fuelmap

import android.os.SystemClock
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.jkane.fuelmap.ui.fuel_entries.FuelEntriesActivity
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FuelEntriesActivityTests {

    @get:Rule
    val activityRule = ActivityTestRule(FuelEntriesActivity::class.java)

    @Test
    fun `nav_footer_is_displayed`() {
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
        onView(
            allOf(
                isDescendantOfA(withId(R.id.nav_view)),
                withId(R.id.fuel_list)
            )
        ).check(matches(isDisplayed()))
        onView(
            allOf(
                isDescendantOfA(withId(R.id.nav_view)),
                withId(R.id.fuel_map)
            )
        ).check(matches(isDisplayed()))
    }
}