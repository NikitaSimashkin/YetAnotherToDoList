package com.example.yetanothertodolist.ui.model

import com.example.yetanothertodolist.ui.view.addFragment.Importance
import java.io.Serializable
import java.time.LocalDateTime

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