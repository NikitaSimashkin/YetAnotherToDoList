package com.example.yetanothertodolist.data.model

data class ServerList(
    val status: String,
    val list: List<ServerTodoItem>,
    val revision: Long? = null
)