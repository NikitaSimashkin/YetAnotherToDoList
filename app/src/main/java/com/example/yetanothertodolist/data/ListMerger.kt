package com.example.yetanothertodolist.data

import android.content.SharedPreferences
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.di.ApplicationScope
import com.example.yetanothertodolist.other.ConstValues
import java.time.LocalDateTime
import javax.inject.Inject

@ApplicationScope
class ListMerger @Inject constructor(private val sharedPreferences: SharedPreferences) {

    private var serverMap: LinkedHashMap<String, TodoItem> = LinkedHashMap()
    private var databaseMap: LinkedHashMap<String, TodoItem> = LinkedHashMap()
    private lateinit var lastSynchronizeTime: LocalDateTime

    suspend fun merge(serverList: List<TodoItem>?, dataBaseList: List<TodoItem>?): List<TodoItem> {
        val tmpString = sharedPreferences.getString(ConstValues.LAST_SYNCHRONIZE_TIME, "")!!
        lastSynchronizeTime = if (tmpString.isEmpty())
            LocalDateTime.now()
        else
            LocalDateTime.parse(tmpString)

        serverList?.forEach { serverMap[it.id] = it }
        dataBaseList?.forEach { databaseMap[it.id] = it }

        if (serverMap.isEmpty()) return databaseMap.values.toList()

        checkDeletedItem()
        addOtherItems()

        sharedPreferences.edit()
            .putString(ConstValues.LAST_SYNCHRONIZE_TIME, LocalDateTime.now().toString()).apply()

        return serverMap.values.toList()
    }

    private fun addOtherItems() {
        databaseMap.values.forEach {
            if (!it.isDeleted && it.createdAt.isAfter(lastSynchronizeTime)) {
                serverMap[it.id] = it
            }
        }
    }

    private suspend fun checkDeletedItem() {
        val copy = LinkedHashMap<String, TodoItem>()
        for (i in serverMap.entries){
            copy[i.key] = i.value
        }
        serverMap.values.forEach {
            if (databaseMap.containsKey(it.id)) {
                val bdItem = databaseMap[it.id]!!
                if (bdItem.changedAt > it.changedAt) {
                    if (bdItem.isDeleted)
                        copy.remove(it.id)
                    else
                        copy[it.id] = bdItem
                }
                databaseMap.remove(it.id)
            }
        }
        serverMap = copy
    }
}