package com.example.yetanothertodolist.data.room

import androidx.room.*
import com.example.yetanothertodolist.data.model.TodoItem

@Dao
interface TasksDao {

    @Update
    suspend fun updateTask(item: TodoItem)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(item: TodoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllTasks(list: List<TodoItem>)


    @Delete
    suspend fun deleteTask(item: TodoItem)

    @Delete
    suspend fun deleteAllTasks(list: List<TodoItem>)

    @Query("DELETE FROM tasks")
    fun deleteAll()


    @Query("SELECT * FROM tasks")
    fun getTasks(): List<TodoItem>?


}