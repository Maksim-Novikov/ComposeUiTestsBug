package com.example.composeuitestsbug

import android.content.Intent
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class EspressoComposeLinkTest {

  @get:Rule
  val activityScenarioRule by lazy {
    ActivityScenarioRule<MainActivity>(
      Intent(
        InstrumentationRegistry.getInstrumentation().targetContext,
        MainActivity::class.java
      )
    )
  }

  @get:Rule
  val composeTestRule by lazy { createEmptyComposeRule() }

  @Test
  fun testNavigation() {
    val composeIdlingResource = IdlingRegistry.getInstance().resources.first { it.name == "Compose-Espresso link" }
    Espresso.unregisterIdlingResources(composeIdlingResource)

    Espresso.onView(withId(R.id.next_btn)).perform(click())

    Espresso.registerIdlingResources(composeIdlingResource)
    Thread.sleep(1000)

    composeTestRule.onNodeWithTag(MainActivity.EDIT_TAG).performTextInput("test")
    composeTestRule.onNodeWithTag(MainActivity.BTN_TAG).performClick()

    Espresso.unregisterIdlingResources(composeIdlingResource)
    Thread.sleep(1000)

    Espresso.onView(withId(R.id.next_btn)).perform(click())

    Espresso.registerIdlingResources(composeIdlingResource)
    Thread.sleep(1000)

    composeTestRule.onNodeWithTag(MainActivity.EDIT_TAG).assertTextContains("")

  }
}





