package com.example.yetanothertodolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.FiveZeroZeroException
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.FourZeroFourException
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.FourZeroOneException
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.FourZeroZeroException
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoItem
import com.example.yetanothertodolist.Backend.Converter
import com.example.yetanothertodolist.Backend.ServerList
import com.example.yetanothertodolist.Backend.ServerOneElement
import com.example.yetanothertodolist.Backend.YetAnotherAPI
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

    suspend fun updateItem(item: TodoItem) = mutex.withLock{ // некруто, что мы не можем отправить
        // несколько запросов на обновление item'ов параллельно. Например, пользователь тыкает на
        // много галочек и помечает несколько разных тасок как сделанные. А мы их потом по одной
        // обновляем на сервере. Это касается и removeItem/addItem.
        // В качестве упражнения, предлагаю подумать, как это исправить, не сломав синхронизацию с
        // обновлением всего списка.
        val newList: MutableList<TodoItem> = ArrayList(_tasks.value as List<TodoItem>)
        val indexOf = newList.indexOfFirst { it.id == item.id }
        newList[indexOf] = item
        _tasks.postValue(newList)
        println(2)
        updateServerElement(item)
    }

    suspend fun getServerList() = mutex.withLock{ // используется как т.е. updateServerList
        val body = api.getList().body()
        _tasks.postValue(body!!.list.map { Converter.getTodoItem(it) })
        lastRevision = body.revision!!
    }

    suspend fun updateServerList() = mutex.withLock{ // по факту syncListWithServer
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

    private fun checkError(code: Int){
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
