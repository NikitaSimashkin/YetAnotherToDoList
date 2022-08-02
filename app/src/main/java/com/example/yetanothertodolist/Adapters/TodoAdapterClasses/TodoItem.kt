package com.example.yetanothertodolist.Adapters.TodoAdapterClasses

import java.io.Serializable
import java.time.temporal.Temporal

data class TodoItem(
    val id: String,
    val description: String,
    val importance: Importance,
    val isCompleted: Boolean,
    val dateOfCreation: Temporal,
    val deadline: Temporal? = null,
    val dateOfChange: Temporal? = null
) : Serializable
