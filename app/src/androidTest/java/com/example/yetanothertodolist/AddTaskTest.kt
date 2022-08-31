@file:Suppress("IllegalIdentifier")

package com.example.yetanothertodolist

import android.os.SystemClock
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.yetanothertodolist.Interactions.AddInteractions
import com.example.yetanothertodolist.Interactions.ListInteractions
import com.example.yetanothertodolist.ui.view.MainActivity.MainActivity
import com.example.yetanothertodolist.util.atLastPosition
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AddTaskTest {

    @JvmField
    @Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val server = MockWebServer()

    @Before
    fun createServer() {
        server.start(8000)
        val dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(500)
            }
        }
        server.dispatcher = dispatcher
    }

    @After
    fun shutdown() {
        server.shutdown()
    }

    @Test
    fun shouldAddTaskWhenSetDescriptionImportanceAndDate() {
        val randomString = UUID.randomUUID().toString()
        SystemClock.sleep(3500) // ждем когда уйдет снэкбар
        ListInteractions.onPlusButton().perform(click())

        AddInteractions.onDescription().perform(replaceText(randomString))

        AddInteractions.onImportance().perform(click())
        AddInteractions.onImportanceLow().perform(click())

        AddInteractions.onSwitch().perform(click())
        AddInteractions.onDatePicker().perform(PickerActions.setDate(2022, 8, 31))
        AddInteractions.onReady().perform(click())

        AddInteractions.onSaveButton().perform(click())

        ListInteractions.onList().check(matches(isDisplayed())) // проверяем, открылся ли лист

        ListInteractions.onList()
            .check( // проверяем, что на последнем месте появилось нужное задание
                matches(atLastPosition(hasDescendant(withText(randomString))))
            )
    }

    @Test
    fun shouldShowSnackBarWhenTryToAddTaskWithoutDescription(){
        SystemClock.sleep(3500) // ждем когда уйдет снэкбар
        ListInteractions.onPlusButton().perform(click())

        AddInteractions.onSaveButton().perform(click())
        AddInteractions.run{
            onSnackBar().check(matches(isDisplayed()))
            onDescription().check(matches(isDisplayed()))
        }
    }
}