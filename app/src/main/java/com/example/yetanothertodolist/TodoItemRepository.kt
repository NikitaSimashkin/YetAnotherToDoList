package com.example.yetanothertodolist

import com.example.yetanothertodolist.Adapters.Importance
import com.example.yetanothertodolist.Adapters.TodoItem
import java.time.LocalDateTime

class TodoItemRepository {

    private val list: ArrayList<TodoItem> = arrayListOf(
        TodoItem("123", "1", Importance.Low, true, LocalDateTime.now()),
        TodoItem("123", "wrwerw2", Importance.Important, true, LocalDateTime.now()),
        TodoItem("1sdfs23", "3", Importance.Basic, false, LocalDateTime.now()),
        TodoItem("123", "4", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "5", Importance.Low, false, LocalDateTime.now()),
        TodoItem("123", "1", Importance.Low, true, LocalDateTime.now()),
        TodoItem("123", "wrwerw2", Importance.Important, true, LocalDateTime.now()),
        TodoItem("1sdfs23", "3", Importance.Basic, false, LocalDateTime.now()),
        TodoItem("123", "4", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "5", Importance.Low, false, LocalDateTime.now()),
        TodoItem("123", "1", Importance.Low, true, LocalDateTime.now()),
        TodoItem("123", "wrwerw2", Importance.Important, true, LocalDateTime.now()),
        TodoItem("1sdfs23", "3", Importance.Basic, false, LocalDateTime.now()),
        TodoItem("123", "4", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "5", Importance.Low, false, LocalDateTime.now()),
        TodoItem("123", "1", Importance.Low, true, LocalDateTime.now()),
        TodoItem("123", "wrwerw2", Importance.Important, true, LocalDateTime.now()),
        TodoItem("1sdfs23", "3", Importance.Basic, false, LocalDateTime.now()),
        TodoItem("123", "4", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "5", Importance.Low, false, LocalDateTime.now()),
        TodoItem("123", "1", Importance.Low, true, LocalDateTime.now()),
        TodoItem("123", "wrwerw2", Importance.Important, true, LocalDateTime.now()),
        TodoItem("1sdfs23", "3", Importance.Basic, false, LocalDateTime.now()),
        TodoItem("123", "4", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "5", Importance.Low, false, LocalDateTime.now()),
    )

    fun addItem(item: TodoItem){
        list.add(item)
    }

    fun removeItem(item: TodoItem){
        list.remove(item)
    }

    val numberOfCompleted: Int
        get() = list.count{it.isCompleted}

    fun getList(): List<TodoItem>{
        return list
    }

}