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
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
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

        // Делаем кучу пустых ответов с сервера, так как нам не важно что отвечает сервер -
        // добавление/удаление дела работают одинаково при рабочем и не рабочем сервере
        // Это сделано только для того чтобы в тесте не отправлять запросы на реальный сервер
        server.enqueue(MockResponse())
        server.enqueue(MockResponse())
        server.enqueue(MockResponse())
        server.enqueue(MockResponse())
        server.enqueue(MockResponse())
        server.enqueue(MockResponse())
        server.enqueue(MockResponse())
        server.enqueue(MockResponse())
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
        SystemClock.sleep(1000) // он когда тему меняет, не может найти нужные вьюшки
        val firstDescription = Array(1) { "" }
        ListInteractions.onList().perform( // запоминаем текст задания, на которое нажимаем
            actionOnItemAtPosition<TaskHolder>(
                0,
                getText(firstDescription, ListInteractions.getTextViewIdFromTaskHolder())
            )
        )
        ListInteractions.onList().perform(actionOnItemAtPosition<TaskHolder>(0, click()))

        AddInteractions.onDescription().check(matches(withText(firstDescription[0])))   // проверяем, что открылось
                                                                                        // именно то задание
        AddInteractions.onDeleteButton().perform(click()) // удаляем задание

        ListInteractions.onList().run {
            check(matches(isDisplayed())) // проверяем что открылся нужный фрагмент
            check(matches(not(hasDescendant(withText(firstDescription[0])))))
        }
    }

    @Test
    fun shouldNotDeletedEmptyTask() {
        SystemClock.sleep(3500)
        ListInteractions.onPlusButton().perform(click())

        AddInteractions.onDescription().check(matches(isDisplayed())) // проверяем открытие нужного фрагмента

        AddInteractions.onDeleteButton().perform(click()) // нажимаем на кнопку удаления

        AddInteractions.onDescription().check(matches(isDisplayed())) // проверяем, что фрагмен не закрылся
    }
}