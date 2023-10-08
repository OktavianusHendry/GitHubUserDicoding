package com.okta.githubuser.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.okta.githubuser.R
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThemeSwitchActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun testThemeSwitch() {
        Thread.sleep(2000)

        onView(withId(R.id.setting)).perform(click())

        onView(withId(R.id.switch_theme)).perform(click())
    }
}
