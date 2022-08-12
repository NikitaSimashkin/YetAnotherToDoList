package com.example.yetanothertodolist.data.sources

import com.example.yetanothertodolist.data.model.ServerList
import com.example.yetanothertodolist.data.model.ServerOneElement
import com.example.yetanothertodolist.Backend.toServerTodoItem
import com.example.yetanothertodolist.Backend.toTodoItem
import com.example.yetanothertodolist.data.FiveZeroZeroException
import com.example.yetanothertodolist.data.FourZeroFourException
import com.example.yetanothertodolist.data.FourZeroOneException
import com.example.yetanothertodolist.data.FourZeroZeroException
import com.example.yetanothertodolist.ui.model.TodoItem


/**
 * Класс для получения информации
 */
class DataSource(private val api: YetAnotherAPI) {

    private var lastRevision = 0L

    suspend fun getList(): List<TodoItem> {
        val answer = api.getList()
        val newList = answer.body()!!.list.map { it.toTodoItem() }

        checkError(answer.code())
        lastRevision = answer.body()!!.revision!!
        return newList
    }

    suspend fun updateList(list: List<TodoItem>): List<TodoItem> {
        val answer = api.updateList(
            lastRevision, ServerList(
                "ok",
                list.map { it.toServerTodoItem() })
        )

        checkError(answer.code())
        lastRevision = answer.body()!!.revision!!
        return answer.body()!!.list.map { it.toTodoItem() }
    }

    suspend fun getItem(id: String): TodoItem {
        val answer = api.getElement(id)

        checkError(answer.code())
        lastRevision = answer.body()!!.revision!!
        return answer.body()!!.element.toTodoItem()
    }

    suspend fun addItem(item: TodoItem) {
        val answer = api.addElement(
            lastRevision, ServerOneElement(
                "ok",
                item.toServerTodoItem()
            )
        )

        checkError(answer.code())
        lastRevision = answer.body()!!.revision!!
    }

    suspend fun updateItem(item: TodoItem) {
        val answer = api.changeElement(
            lastRevision,
            item.id,
            ServerOneElement(
                "ok",
                item.toServerTodoItem()
            )
        )

        checkError(answer.code())
        lastRevision = answer.body()!!.revision!!
    }

    suspend fun deleteItem(id: String) {
        val answer = api.deleteElement(lastRevision, id)

        checkError(answer.code())
        lastRevision = answer.body()!!.revision!!
    }

    var count = 0
    private suspend fun checkError(code: Int) {
        when (code) {
            400 -> throw FourZeroZeroException()
            401 -> throw FourZeroOneException()
            404 -> throw FourZeroFourException()
            500 -> throw FiveZeroZeroException()
        }
    }
}