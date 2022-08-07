package com.example.yetanothertodolist.Backend

import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.Importance
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*

class Converter private constructor(){
    companion object{
        fun getTodoItem(serverItem: ServerTodoItem): TodoItem{
            return TodoItem(
                id = serverItem.id,
                description = serverItem.description,
                importance = Importance.valueOf(serverItem.importance.replaceFirstChar { it.uppercase() }),
                done = serverItem.done,
                createdAt = serverItem.createdAt.toLocalDateTime(),
                deadline = serverItem.deadline?.toLocalDateTime(),
                changedAt = serverItem.changedAt.toLocalDateTime(),
                color = serverItem.color,
                lastUpdateBy = serverItem.lastUpdatedBy
            )
        }

        fun getServerTodoItem(todoItem: TodoItem): ServerTodoItem{
            return ServerTodoItem(
                id = todoItem.id,
                description = todoItem.description,
                importance = todoItem.importance.toString().lowercase(),
                done = todoItem.done,
                createdAt = todoItem.createdAt.toSeconds(),
                deadline = todoItem.deadline?.toSeconds(),
                changedAt = todoItem.changedAt.toSeconds(),
                color = todoItem.color,
                lastUpdatedBy = todoItem.lastUpdateBy
            )
        }
    }
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)
}

fun LocalDateTime.toSeconds(): Long{
    return this.toEpochSecond(ZoneOffset.UTC)
}