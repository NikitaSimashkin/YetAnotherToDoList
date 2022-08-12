package com.example.yetanothertodolist.data.model

import com.google.gson.annotations.SerializedName

data class ServerTodoItem(
    val id: String,

    @SerializedName("text")
    val description: String,

    val importance: String,

    val deadline: Long? = null,

    val done: Boolean,

    val color: String? = null,

    @SerializedName("created_at")
    val createdAt: Long,

    @SerializedName("changed_at")
    val changedAt: Long,

    @SerializedName("last_updated_by")
    val lastUpdatedBy: String

)