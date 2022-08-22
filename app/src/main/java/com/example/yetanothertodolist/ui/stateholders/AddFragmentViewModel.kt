package com.example.yetanothertodolist.ui.stateholders

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yetanothertodolist.YetAnotherApplication
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.ui.view.addFragment.Importance
import com.example.yetanothertodolist.util.ConnectiveLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

/**
 * viewModel для сохранения состояния фрагмента добавления
 */
class AddFragmentViewModel(
    private val repository: TodoItemRepository,
    private val connectiveLiveData: ConnectiveLiveData,
) : ViewModel() {
    var id: String = ""

    var description: String = ""

    var importance: Importance = Importance.Basic

    var done: Boolean = false

    var createdAt: LocalDateTime = LocalDateTime.now()

    var deadline: LocalDateTime? = null

    var changedAt: LocalDateTime = LocalDateTime.now()

    var color: String? = null

    var lastUpdateBy: String = "Me"

    var isDeleted = false

    private val observer: Observer<Boolean> =
        Observer<Boolean> { value -> isConnected = value}
    private var isConnected: Boolean = connectiveLiveData.value ?: false

    init {
        connectiveLiveData.observeForever(observer)
    }

    override fun onCleared() {
        super.onCleared()
        connectiveLiveData.removeObserver(observer)
    }

    private fun setStartValues() {
        id = UUID.randomUUID().toString()
        description = ""
        importance = Importance.Basic
        done = false
        createdAt = LocalDateTime.now()
        deadline = null
        changedAt = LocalDateTime.now()
        color = null
        lastUpdateBy = YetAnotherApplication.deviceId
        isDeleted = false
    }

    private fun setItemValues(item: TodoItem) {
        id = item.id
        description = item.description
        importance = item.importance
        done = item.done
        createdAt = item.createdAt
        deadline = item.deadline
        changedAt = item.changedAt
        color = item.color
        lastUpdateBy = item.lastUpdateBy
        isDeleted = item.isDeleted
    }

    private fun getItem(
        isNewTask: Boolean,
        isDeleted: Boolean = false
    ): TodoItem {
        val changedAt = LocalDateTime.now()
        return TodoItem(
            id = id,
            description = description,
            importance = importance,
            done = done,
            createdAt = if (isNewTask) changedAt else createdAt,
            deadline = deadline,
            changedAt = changedAt,
            color = color,
            lastUpdateBy = YetAnotherApplication.deviceId,
            isDeleted = isDeleted
        )
    }


    fun addItem(): TodoItem {
        val item = getItem(true)
        viewModelScope.launch(Dispatchers.IO) { repository.addItem(item) }
        clear()

        return item
    }

    fun deleteItem(item: TodoItem) {
        if (isConnected)
            viewModelScope.launch(Dispatchers.IO) { repository.removeItem(item) }
        else
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateItem(
                    item.copy(
                        isDeleted = true,
                        changedAt = LocalDateTime.now(),
                        lastUpdateBy = YetAnotherApplication.deviceId
                    )
                )
            }
        clear()
    }

    fun updateItem(): TodoItem {
        val item = getItem(false)
        viewModelScope.launch(Dispatchers.IO) { repository.updateItem(item) }
        clear()

        return item
    }


    private var valuesAlreadySet = false

    fun setTask(task: TodoItem?) {
        if (!valuesAlreadySet) {
            if (task == null) setStartValues()
            else setItemValues(task)
            valuesAlreadySet = true
        }
    }

    fun clear() {
        valuesAlreadySet = false
    }
}