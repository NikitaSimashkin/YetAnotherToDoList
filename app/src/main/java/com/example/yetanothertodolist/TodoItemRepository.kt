package com.example.yetanothertodolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.*
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.MyInternetException.*
import com.example.yetanothertodolist.Backend.Converter
import com.example.yetanothertodolist.Backend.ServerList
import com.example.yetanothertodolist.Backend.ServerOneElement
import com.example.yetanothertodolist.Backend.YetAnotherAPI
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class TodoItemRepository(private val api: YetAnotherAPI) {

    private val _tasks = MutableLiveData<List<TodoItem>>(ArrayList<TodoItem>())
    var internet : MutableLiveData<Boolean>? = null

    val tasks: LiveData<List<TodoItem>> = _tasks
    val mutex = Mutex()

    var lastRevision = 12L

    suspend fun addItem(item: TodoItem) {
        mutex.withLock {
            val newList: MutableList<TodoItem> = ArrayList(_tasks.value as List<TodoItem>)
            newList.add(item)
            _tasks.postValue(newList)

            addServerElement(item)
        }
    }

    suspend fun removeItem(item: TodoItem) = mutex.withLock{
        val newList: MutableList<TodoItem> = ArrayList(_tasks.value as List<TodoItem>)
        newList.removeIf { it.id == item.id }
        _tasks.postValue(newList)

        deleteServerElement(item.id)
    }

    suspend fun updateItem(item: TodoItem) = mutex.withLock{
        val newList: MutableList<TodoItem> = ArrayList(_tasks.value as List<TodoItem>)
        val element = newList.find { it.id == item.id }
        val indexOf = newList.indexOf(element)
        newList[indexOf] = item
        _tasks.postValue(newList)
        println(2)
        updateServerElement(item)
    }

    suspend fun getServerList() = mutex.withLock{
        val body = api.getList().body()
        _tasks.postValue(body!!.list.map { Converter.getTodoItem(it) })
        lastRevision = body.revision!!
    }

    suspend fun updateServerList() = mutex.withLock{
        val body = api.updateServerList(lastRevision, ServerList(
            "ok",
            _tasks.value!!.map { Converter.getServerTodoItem(it) }
        )).body()

        lastRevision = body!!.revision!!
        _tasks.postValue(body.list.map { Converter.getTodoItem(it) })
    }

    private suspend fun getServerElement(id: String): TodoItem {
        val response = api.getElement(id)
        checkError(response.code())
        return Converter.getTodoItem(response.body()!!.element)
    }

    suspend fun addServerElement(item: TodoItem, withoutError: Boolean = false) {
        val body = api.addElement(
            lastRevision, ServerOneElement(
                "ok",
                Converter.getServerTodoItem(item)
            )
        )
        if (!withoutError)
            checkError(body.code())
        lastRevision = body.body()!!.revision!!
    }

    suspend fun updateServerElement(item: TodoItem, withoutError: Boolean = false) {
        val body = api.changeElement(
            lastRevision,
            item.id,
            ServerOneElement(
                "ok",
                Converter.getServerTodoItem(item)
            )
        )
        if (!withoutError)
            checkError(body.code())
        lastRevision = body.body()!!.revision!!
    }

    suspend fun deleteServerElement(id: String, withoutError: Boolean = false){
        val body = api.deleteElement(lastRevision, id)

        if (!withoutError)
            checkError(body.code())
        lastRevision = body.body()!!.revision!!
    }

    private suspend fun checkError(code: Int){
        when (code){
            400 -> throw FourZeroZeroException()
            401 -> throw FourZeroOneException()
            404 -> throw FourZeroFourException()
            500 -> throw FiveZeroZeroException()
        }
    }

    val numberOfCompleted: Int
        get() = _tasks.value?.count { it.done } ?: 0

}