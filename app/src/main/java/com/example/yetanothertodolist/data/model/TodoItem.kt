package com.example.yetanothertodolist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.yetanothertodolist.data.room.Converters
import com.example.yetanothertodolist.ui.view.addFragment.Importance
import java.io.Serializable
import java.time.LocalDateTime

@Entity(
    tableName = "tasks"
)
@TypeConverters(Converters::class)
data class TodoItem(
    @PrimaryKey val id: String,
    val description: String,
    val importance: Importance,
    val done: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    val deadline: LocalDateTime?,
    @ColumnInfo(name = "changed_at") val changedAt: LocalDateTime,
    val color: String?,
    @ColumnInfo(name = "lat_updated_at") val lastUpdateBy: String,
    @ColumnInfo(name = "is_deleted", defaultValue = "0") val isDeleted: Boolean
): Serializable