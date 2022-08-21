package com.example.yetanothertodolist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yetanothertodolist.data.ListMerger
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.data.sources.DataBaseSource
import com.example.yetanothertodolist.data.sources.ServerSource
import com.example.yetanothertodolist.di.ApplicationScope
import com.example.yetanothertodolist.util.ErrorManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *  Через этот класс идет вся работа с данными
 */
@ApplicationScope
class TodoItemRepository @Inject constructor(
    private val serverSource: ServerSource,
    private val dataBaseSource: DataBaseSource,
    private val listMerger: ListMerger
) {

    var errorManager: ErrorManager? = null

    private val _tasks = MutableLiveData<List<TodoItem>>(emptyList())
    val tasks: LiveData<List<TodoItem>> = _tasks

    private val mutex = Mutex()

    suspend fun addItem(item: TodoItem) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val newList = _tasks.value.orEmpty().toMutableList().also { it.add(item) }
                setNewList(newList)
            }
            dataBaseSource.addItem(item)
            errorManager?.launchWithHandler { serverAdd(item) }
        }
    }

    suspend fun removeItem(item: TodoItem) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val newList = _tasks.value.orEmpty().toMutableList()
                newList.removeIf { it.id == item.id }
                setNewList(newList)
            }
            dataBaseSource.deleteItem(item)
            errorManager?.launchWithHandler { serverRemove(item) }
        }
    }

    suspend fun updateItem(item: TodoItem) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val newList = _tasks.value.orEmpty().toMutableList()
                newList[newList.indexOfFirst { it.id == item.id }] = item
                setNewList(newList.filter {!it.isDeleted})
            }
            dataBaseSource.updateItem(item)
            if (!item.isDeleted)
                errorManager?.launchWithHandler { serverUpdate(item) }
        }
    }


    suspend fun updateList() {
        val newList = listMerger.merge(serverGetList(), dataBaseGetList())
        setDataBaseList(newList)
        errorManager?.launchWithHandler { serverUpdateList(newList) }
        setNewList(newList.filter{!it.isDeleted})
    }


    private suspend fun setDataBaseList(list: List<TodoItem>){
        dataBaseSource.deleteAll()
        dataBaseSource.addAll(list)
    }

    private suspend fun dataBaseGetList(): List<TodoItem>? {
        return dataBaseSource.getTask()
    }

    private suspend fun serverGetList(): List<TodoItem>? {
        var newList: List<TodoItem>? = null
        try {
            newList = serverSource.getList()
        } catch(ignored: Exception){}

        return newList
    }

    private suspend fun serverUpdateList(list: List<TodoItem>) = serverSource.updateList(list)

    private suspend fun serverUpdate(item: TodoItem) = serverSource.updateItem(item)

    private suspend fun serverAdd(item: TodoItem) = serverSource.addItem(item)

    private suspend fun serverRemove(item: TodoItem) = serverSource.deleteItem(item.id)


    // Только для WorkManager
    suspend fun serverGetListWithoutErrorManager() {
        val newList = try {
            serverSource.getList()
        } catch (e: Exception) {
            null
        }
        if (newList != null) {
            dataBaseSource.deleteAll(_tasks.value!!)
            setNewList(newList)
            dataBaseSource.addAll(newList)
        }
    }

    private suspend fun setNewList(newList: List<TodoItem>) {
        withContext(Dispatchers.Main) { _tasks.value = newList }
    }
}