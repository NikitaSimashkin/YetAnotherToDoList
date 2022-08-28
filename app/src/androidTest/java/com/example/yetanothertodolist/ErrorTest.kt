package com.example.yetanothertodolist

import android.os.SystemClock
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.yetanothertodolist.Interactions.AddInteractions
import com.example.yetanothertodolist.Interactions.ListInteractions
import com.example.yetanothertodolist.ui.view.MainActivity.MainActivity
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

// проверены все возможные ошибки с сервера

@RunWith(AndroidJUnit4::class)
class ErrorTest {

    @JvmField
    @Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val server = MockWebServer()

    @Before
    fun createServer() {
        server.start(8000)
    }

    // проверяем коды ошибок 401 и 500
    @Test
    fun shouldShowRetrySnackBarWhenAddTask() {
        val dispatcher = object: Dispatcher(){
            var count = 0
            override fun dispatch(request: RecordedRequest): MockResponse {
                val code = if(count % 2 == 0) 401 else 500
                count++
                return MockResponse().setResponseCode(code)
            }
        }
        server.dispatcher = dispatcher

        // не считаю плохим так делать со снекбарами, ведь у них время фиксированное
        // мы не просто надеямся, что снэкбар уйдет, мы знает что снэкбар уйдет
        SystemClock.sleep(3500)
        ListInteractions.onPlusButton().perform(click())
        AddInteractions.onDescription().perform(replaceText(randomString()))
        AddInteractions.onSaveButton().perform(click())
        ListInteractions.onSnackBar401And500().check(matches(isDisplayed()))

        SystemClock.sleep(3500)
        ListInteractions.onPlusButton().perform(click())
        AddInteractions.onDescription().perform(replaceText(randomString()))
        AddInteractions.onSaveButton().perform(click())
        ListInteractions.onSnackBar401And500().check(matches(isDisplayed()))
    }


    // проверяем непонятные ошибки
    @Test
    fun shouldShowSnackBarWithoutRetry() {
        val dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
            }
        }
        server.dispatcher = dispatcher

        SystemClock.sleep(3500)
        ListInteractions.onPlusButton().perform(click())
        AddInteractions.onDescription().perform(replaceText(randomString()))
        AddInteractions.onSaveButton().perform(click())
        ListInteractions.onSnackBarUnknown().check(matches(isDisplayed()))

        SystemClock.sleep(3500)
        ListInteractions.onPlusButton().perform(click())
        AddInteractions.onDescription().perform(replaceText(randomString()))
        AddInteractions.onSaveButton().perform(click())
        ListInteractions.onSnackBarUnknown().check(matches(isDisplayed()))
    }

    // проверяем коды ошибок 400 и 404
    @Test
    fun shouldNotShowSnackBarWhenAddTask() {
        val dispatcher = object: Dispatcher(){
            var count = 0
            override fun dispatch(request: RecordedRequest): MockResponse {
                val code = if(count % 2 == 0) 400 else 404
                count++
                return MockResponse().setResponseCode(code)
            }

        }
        server.dispatcher = dispatcher

        SystemClock.sleep(3500)
        ListInteractions.onPlusButton().perform(click())
        AddInteractions.onDescription().perform(replaceText(randomString()))
        AddInteractions.onSaveButton().perform(click())

        // эти два действия - проверка на то что снэкбара нет, ведь в противном случае кнопка добавления не нажмется
        // Другого способа проверить отсутствия снэкбара я не придумал(
        ListInteractions.onPlusButton().perform(click())
        AddInteractions.onCancelButton().perform(click())

        SystemClock.sleep(3500)
        ListInteractions.onPlusButton().perform(click())
        AddInteractions.onDescription().perform(replaceText(randomString()))
        AddInteractions.onSaveButton().perform(click())
        ListInteractions.onPlusButton().perform(click())
        AddInteractions.onCancelButton().perform(click())
    }


    @After
    fun shutdown() {
        server.shutdown()
    }

    fun randomString() = UUID.randomUUID().toString()
}