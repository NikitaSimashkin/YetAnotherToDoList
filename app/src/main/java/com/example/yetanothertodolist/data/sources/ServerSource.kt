package com.example.yetanothertodolist.data.sources

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.yetanothertodolist.Backend.toServerTodoItem
import com.example.yetanothertodolist.Backend.toTodoItem
import com.example.yetanothertodolist.data.FiveZeroZeroException
import com.example.yetanothertodolist.data.FourZeroFourException
import com.example.yetanothertodolist.data.FourZeroOneException
import com.example.yetanothertodolist.data.FourZeroZeroException
import com.example.yetanothertodolist.data.model.ServerList
import com.example.yetanothertodolist.data.model.ServerOneElement
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.di.ApplicationScope
import com.example.yetanothertodolist.other.ConstValues
import java.time.LocalDateTime
import javax.inject.Inject


/**
 * Класс для получения информации с сервера
 */
@ApplicationScope
class ServerSource @Inject constructor(
    private val api: YetAnotherAPI,
    private val sharedPreferences: SharedPreferences
) {

    private var lastRevision = sharedPreferences.getLong(ConstValues.LAST_REVISION_DB, 0)

    suspend fun getList(): List<TodoItem>? {
        val answer = api.getList()
        val newList = answer.body()?.list?.map { it.toTodoItem() }
        checkError(answer.code())
        updateRevision(answer.body()!!.revision!!)
        return newList
    }

    suspend fun updateList(list: List<TodoItem>): List<TodoItem> {
        val answer = api.updateList(
            lastRevision, ServerList(
                "ok",
                list.map { it.toServerTodoItem() })
        )

        checkError(answer.code())
        updateLastSync()
        updateRevision(answer.body()!!.revision!!)
        return answer.body()!!.list.map { it.toTodoItem() }
    }

    suspend fun addItem(item: TodoItem) {
        val answer = api.addElement(
            lastRevision, ServerOneElement(
                "ok",
                item.toServerTodoItem()
            )
        )

        checkError(answer.code())
        updateLastSync()
        answer.body()?.revision?.let { updateRevision(it) }
    }

    suspend fun deleteItem(id: String) {
        val answer = api.deleteElement(lastRevision, id)

        checkError(answer.code())
        updateLastSync()
        updateRevision(answer.body()!!.revision!!)
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
        updateLastSync()
        updateRevision(answer.body()!!.revision!!)
    }

    private fun updateRevision(newRevision: Long) {
        lastRevision = newRevision
        sharedPreferences.edit {
            putLong(ConstValues.LAST_REVISION_DB, lastRevision)
        }
    }

    private fun updateLastSync(){
        sharedPreferences.edit {
            putString(ConstValues.LAST_SYNCHRONIZE_TIME, LocalDateTime.now().toString())
        }
    }

    private fun checkError(code: Int) {
        when (code) {
            400 -> throw FourZeroZeroException()
            401 -> throw FourZeroOneException()
            404 -> throw FourZeroFourException()
            500 -> throw FiveZeroZeroException()
        }
        //throw FiveZeroZeroException()
    }
}