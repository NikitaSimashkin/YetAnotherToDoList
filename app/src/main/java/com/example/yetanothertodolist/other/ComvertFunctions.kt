package com.example.yetanothertodolist.Backend

import com.example.yetanothertodolist.data.model.ServerTodoItem
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.ui.view.addFragment.Importance
import java.time.LocalDateTime
import java.time.ZoneOffset

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
        lastUpdateBy = lastUpdatedBy,
        isDeleted = false
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

