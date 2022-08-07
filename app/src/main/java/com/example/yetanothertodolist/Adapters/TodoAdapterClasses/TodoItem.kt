package com.example.yetanothertodolist.Adapters.TodoAdapterClasses

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.Temporal

data class TodoItem(

    val id: String,

    val description: String,

    val importance: Importance,

    val done: Boolean,

    val createdAt: LocalDateTime,

    val deadline: LocalDateTime? = null,

    val changedAt: LocalDateTime,

    val color: String? = null,

    val lastUpdateBy: String

) : Serializable
