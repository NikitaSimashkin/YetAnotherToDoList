package com.example.yetanothertodolist

import android.content.Context
import android.content.ReceiverCallNotAllowedException
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.Importance
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.MyInternetException
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoItem
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import java.time.LocalDateTime
import kotlin.random.Random

class TodoItemRepository {

    private val _tasks = MutableLiveData<List<TodoItem>>(
        arrayListOf(
            TodoItem("1", "1", Importance.Low, true, LocalDateTime.now()),
            TodoItem("2", "2", Importance.Important, false, LocalDateTime.now()),
            TodoItem("3", "3", Importance.Basic, false, LocalDateTime.now()),
            TodoItem("4", "4", Importance.Basic, true, LocalDateTime.now()),
            TodoItem("5", "fsfsfsdfsdfsddddddddddddd5", Importance.Low, false, LocalDateTime.now()),
            TodoItem("6", "1", Importance.Low, true, LocalDateTime.now()),
            TodoItem("7", "wrwerw2", Importance.Important, true, LocalDateTime.now()),
            TodoItem("8", "3", Importance.Basic, false, LocalDateTime.now()),
            TodoItem("9", "4", Importance.Basic, true, LocalDateTime.now()),
            TodoItem("10", "5", Importance.Low, false, LocalDateTime.now()),
            TodoItem("11", "1", Importance.Low, true, LocalDateTime.now()),
            TodoItem("12", "wrwerw2", Importance.Important, true, LocalDateTime.now()),
            TodoItem("13", "3", Importance.Basic, false, LocalDateTime.now()),
            TodoItem("14", "4", Importance.Basic, true, LocalDateTime.now()),
            TodoItem(
                "15",
                "5aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                Importance.Low,
                false,
                LocalDateTime.now()
            ),
            TodoItem("16", "1", Importance.Low, true, LocalDateTime.now()),
            TodoItem("17", "wrwerw2", Importance.Important, true, LocalDateTime.now()),
            TodoItem("18", "3", Importance.Basic, false, LocalDateTime.now()),
            TodoItem("19", "4", Importance.Basic, true, LocalDateTime.now()),
            TodoItem("20", "5", Importance.Low, false, LocalDateTime.now()),
            TodoItem(
                "21",
                "1ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd",
                Importance.Low,
                true,
                LocalDateTime.now()
            ),
            TodoItem("22", "wrwerw2", Importance.Important, true, LocalDateTime.now()),
            TodoItem("23", "3", Importance.Basic, false, LocalDateTime.now()),
            TodoItem("24", "4", Importance.Basic, true, LocalDateTime.now()),
            TodoItem("25", "5", Importance.Low, false, LocalDateTime.now()),
        )
    )

    val tasks: LiveData<List<TodoItem>> = _tasks
    val mutex = Mutex()

    suspend fun addItem(item: TodoItem) {
        val newList: MutableList<TodoItem> = ArrayList(_tasks.value as List<TodoItem>)
        newList.add(item)
        _tasks.postValue(newList)

        delay(1000L)
        if (Random.nextBoolean())
            throw MyInternetException()
    }

    suspend fun removeItem(item: TodoItem) {
        val newList: MutableList<TodoItem> = ArrayList(_tasks.value as List<TodoItem>)
        newList.remove(item)
        _tasks.postValue(newList)

        delay(1000L)
        if (Random.nextBoolean())
            throw MyInternetException()
    }

    suspend fun updateItem(item: TodoItem) {
        val newList: MutableList<TodoItem> = ArrayList(_tasks.value as List<TodoItem>)
        val element = newList.find { it.id == item.id }
        val indexOf = newList.indexOf(element)
        newList[indexOf] = item
        _tasks.postValue(newList)

        delay(1000L)
        println("Запрос на сервер")
        println(item.isCompleted)
        if (Random.nextBoolean())
            throw MyInternetException()
    }

    val numberOfCompleted: Int
        get() = _tasks.value?.count { it.isCompleted } ?: 0

}