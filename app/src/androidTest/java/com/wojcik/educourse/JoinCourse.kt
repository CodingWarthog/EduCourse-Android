package com.wojcik.educourse

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.wojcik.educourse.login.activities.LoginActivity
import android.support.v7.widget.RecyclerView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JoinCourse {

    @Rule
    @JvmField
    val rule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun joinCourse() {
        onView(ViewMatchers.withId(R.id.loginBtn)).perform(click())
        Thread.sleep(2000)
        onView(ViewMatchers.withId(R.id.quizzesBtn)).perform(click())
        Thread.sleep(2000)
        onView(ViewMatchers.withId(R.id.available_courses_btn)).perform(click())
        Thread.sleep(2000)
        onView(ViewMatchers.withId(R.id.recyclerViewCourses)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(2000)
        onView(ViewMatchers.withText("TAK")).perform(click())
    }
}