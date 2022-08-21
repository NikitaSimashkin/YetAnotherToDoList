package com.example.yetanothertodolist.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yetanothertodolist.data.model.TodoItem

@Database(
    version = 2,
    entities = [TodoItem::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun getDao(): TasksDao
}