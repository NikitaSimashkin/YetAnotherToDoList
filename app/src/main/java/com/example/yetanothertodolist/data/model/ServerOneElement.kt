package com.example.yetanothertodolist.data.model

data class ServerOneElement(
    val status: String,
    val element: ServerTodoItem,
    val revision: Long? = null
)

