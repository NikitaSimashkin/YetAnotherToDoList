package com.example.yetanothertodolist

import android.os.SystemClock
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.yetanothertodolist.Interactions.AddInteractions
import com.example.yetanothertodolist.Interactions.ListInteractions
import com.example.yetanothertodolist.ui.view.MainActivity.MainActivity
import com.example.yetanothertodolist.ui.view.listFragment.recycler.TaskHolder
import com.example.yetanothertodolist.util.getText
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeleteTaskTest {

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

    /**
     * Тест не работает, если в списке нет элементов, но это и логично, ведь чтобы проверить удаление, нужно чтоб было что удалять
     */
    @Test
    fun shouldDeleteCertainTaskWhenPressDeleteButton() {
        SystemClock.sleep(1000)
        val firstDescription = Array(1) { "" }
        ListInteractions.onList().perform(
            actionOnItemAtPosition<TaskHolder>(
                0,
                getText(firstDescription, ListInteractions.getTextViewIdFromTaskHolder())
            )
        )
        ListInteractions.onList().perform(actionOnItemAtPosition<TaskHolder>(0, click()))

        AddInteractions.onDescription().check(matches(withText(firstDescription[0])))

        AddInteractions.onDeleteButton().perform(click())

        ListInteractions.onList().run {
            check(matches(isDisplayed()))
            check(matches(not(hasDescendant(withText(firstDescription[0])))))
        }
    }

    @Test
    fun shouldNotDeletedEmptyTask() {
        SystemClock.sleep(3500)
        ListInteractions.onPlusButton().perform(click())

        AddInteractions.onDescription().check(matches(isDisplayed()))

        AddInteractions.onDeleteButton().perform(click())

        AddInteractions.onDescription().check(matches(isDisplayed()))
    }
}