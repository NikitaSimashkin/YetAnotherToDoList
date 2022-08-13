package com.example.yetanothertodolist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yetanothertodolist.data.sources.DataSource
import com.example.yetanothertodolist.ui.model.TodoItem
import com.example.yetanothertodolist.other.ErrorManager
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 *  Через этот класс идет вся работа с данными
 */
class TodoItemRepository(
    private val dataSource: DataSource,
) {

    /**
     * Без менеджера ошибок репозиторий будет выполнять свои задачи, но только локально.
     * Если нужно отправлять данные на сервер - требуется задать errorManager.
     * Поэтому тут нельзя сказать "ты не прав, все обязательные для работы объекты
     * должны быть в конструкторе" так как ErrorManager не обязательный - без него репозиторий
     * не сломается
     */
    var errorManager: ErrorManager? = null

    private val _tasks = MutableLiveData<List<TodoItem>>(ArrayList())
    val tasks: LiveData<List<TodoItem>> = _tasks

    private val mutex = Mutex()

    suspend fun addItem(item: TodoItem) = mutex.withLock {
        withContext(Dispatchers.IO) {
            val newList = ArrayList(_tasks.value as List<TodoItem>).also { it.add(item) }
            withContext(Dispatchers.Main){_tasks.value = newList}
            errorManager?.launchWithHandler {serverAdd(item)}
        }
    }

    suspend fun removeItem(item: TodoItem) = mutex.withLock {
        withContext(Dispatchers.IO) {
            val newList = ArrayList(_tasks.value as List<TodoItem>)
            newList.removeIf { it.id == item.id }
            withContext(Dispatchers.Main){_tasks.value = newList}
            errorManager?.launchWithHandler {serverRemove(item) }
        }
    }

    suspend fun updateItem(item: TodoItem) = mutex.withLock {
        withContext(Dispatchers.IO) {
            val newList = ArrayList(_tasks.value as List<TodoItem>)
            newList[newList.indexOfFirst { it.id == item.id }] = item
            withContext(Dispatchers.Main){_tasks.value = newList}
            errorManager?.launchWithHandler { serverUpdate(item) }
        }
    }


    suspend fun getList() = mutex.withLock {
        serverGetList()
    }

    suspend fun updateList() = mutex.withLock {
        serverUpdateList()
    }

    suspend fun getItem(id: String): TodoItem {
        var item: TodoItem? = null
        errorManager?.launchWithHandler {
            item = serverGetItem(id)
        }
        return item!!
    }

    suspend fun serverGetItem(id: String) = dataSource.getItem(id)

    suspend fun serverGetList() = errorManager?.launchWithHandler {
        val list = dataSource.getList()
        withContext(Dispatchers.Main){_tasks.value = list}
    }

    suspend fun serverUpdateList() = errorManager?.launchWithHandler {
        val list = dataSource.updateList(_tasks.value!!)
        withContext(Dispatchers.Main){_tasks.value = list}
    }

    suspend fun serverUpdate(item: TodoItem) = dataSource.updateItem(item)

    suspend fun serverAdd(item: TodoItem) = dataSource.addItem(item)

    suspend fun serverRemove(item: TodoItem) = dataSource.deleteItem(item.id)


    /**
     * Да, иногда надо обязательно сходить в сеть, даже если errorManager нету
     * Этот метод создан только для workManager
     */
    suspend fun serverGetListWithoutErrorManager(){
        val list = try { dataSource.getList() } catch (e: Exception) {_tasks.value!!}
        withContext(Dispatchers.Main){_tasks.value = list}
    }
}