package com.example.yetanothertodolist

import com.example.yetanothertodolist.Adapters.Importance
import com.example.yetanothertodolist.Adapters.TodoItem
import java.time.LocalDateTime

object TodoItemRepository {

    val list: MutableList<TodoItem> = arrayListOf(
        TodoItem("123", "1", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "2", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "3", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "4", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "5", Importance.Basic, false, LocalDateTime.now()),
        TodoItem("123", "6", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "7", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "8", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd9", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "10", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "11", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "12", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "13", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "14", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "15", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "16", Importance.Basic, true, LocalDateTime.now()),
        TodoItem("123", "17", Importance.Basic, true, LocalDateTime.now()),
    )

    fun addItem(item: TodoItem){
        list.add(item)
    }

}