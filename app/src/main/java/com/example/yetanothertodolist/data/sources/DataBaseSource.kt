package com.example.yetanothertodolist.data.sources

import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.data.room.TasksDao
import javax.inject.Inject


class DataBaseSource @Inject constructor(
    private val dao: TasksDao
) {

    suspend fun updateItem(item: TodoItem){
        dao.updateTask(item)
    }

    suspend fun addItem(item: TodoItem){
        dao.addTask(item)
    }

    suspend fun addAll(list: List<TodoItem>){
        dao.addAllTasks(list)
    }

    suspend fun deleteItem(item: TodoItem){
        dao.deleteTask(item)
    }

    fun deleteAll(){
        dao.deleteAll()
    }

    fun getTask(): List<TodoItem>?{
        return dao.getTasks()
    }
}
