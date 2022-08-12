package com.example.yetanothertodolist.Backend

import com.example.yetanothertodolist.ui.view.addFragment.Importance
import com.example.yetanothertodolist.ui.model.TodoItem
import com.example.yetanothertodolist.data.model.ServerTodoItem
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Функции для преобразования одного объекта в другой
 * В джаве я бы сделал отдельный класс со static методами, но котлин позволяет упростить это
 */
fun ServerTodoItem.toTodoItem(): TodoItem {
    return TodoItem(
        id = id,
        description = description,
        importance = Importance.valueOf(importance.replaceFirstChar { it.uppercase() }),
        done = done,
        createdAt = createdAt.toLocalDateTime(),
        deadline = deadline?.toLocalDateTime(),
        changedAt = changedAt.toLocalDateTime(),
        color = color,
        lastUpdateBy = lastUpdatedBy
    )
}

fun TodoItem.toServerTodoItem(): ServerTodoItem {
    return ServerTodoItem(
        id = id,
        description = description,
        importance = importance.toString().lowercase(),
        done = done,
        createdAt = createdAt.toSeconds(),
        deadline = deadline?.toSeconds(),
        changedAt = changedAt.toSeconds(),
        color = color,
        lastUpdatedBy = lastUpdateBy
    )
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)
}

fun LocalDateTime.toSeconds(): Long{
    return this.toEpochSecond(ZoneOffset.UTC)
}

