package com.wojcik.educourse

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.wojcik.educourse.flashcards.FlashcardsActivity
import com.wojcik.educourse.flashcards.FlashcardsFieldActivity
import com.wojcik.educourse.flashcards.FlashcardsSetCreator

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    val rule: ActivityTestRule<FlashcardsSetCreator> = ActivityTestRule(FlashcardsSetCreator::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.wojcik.educourse", appContext.packageName)
    }

    @Test
    fun add_new_flashcard_set() {
        onView(withId(R.id.set_name)).perform(typeText("8756458"))
        onView(withId(R.id.set_description)).perform(typeText("Opis"))
        onView(withId(R.id.set_btn)).perform(click())
        Thread.sleep(2000)
        onView(ViewMatchers.withText("8756458")).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.menu_flashcard_field)).perform((click()))
        Thread.sleep(1000)
        onView(ViewMatchers.withText("Usuń zestaw")).perform(click())
        onView(ViewMatchers.withText("TAK")).perform(click())
    }
}
