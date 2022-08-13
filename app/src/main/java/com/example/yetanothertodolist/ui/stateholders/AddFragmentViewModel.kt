package com.example.yetanothertodolist.ui.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.ui.model.TodoItem
import com.example.yetanothertodolist.ui.view.addFragment.Importance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

/**
 * viewModel для сохранения состояния фрагмента добавления
 */
class AddFragmentViewModel(private val repository: TodoItemRepository) : ViewModel() {
    var id: String = ""

    var description: String = ""

    var importance: Importance = Importance.Basic

    var done: Boolean = false

    var createdAt: LocalDateTime = LocalDateTime.now()

    var deadline: LocalDateTime? = null

    var changedAt: LocalDateTime = LocalDateTime.now()

    var color: String? = null

    var lastUpdateBy: String = "Me"

    var valuesAlreadySet = false

    fun setStartValues() {
        id = UUID.randomUUID().toString()
        description = ""
        importance = Importance.Basic
        done = false
        createdAt = LocalDateTime.now()
        deadline = null
        changedAt = LocalDateTime.now()
        color = null
        lastUpdateBy = "Me"
    }

    fun setItemValues(item: TodoItem) {
        id = item.id
        description = item.description
        importance = item.importance
        done = item.done
        createdAt = item.createdAt
        deadline = item.deadline
        changedAt = item.changedAt
        color = item.color
        lastUpdateBy = item.lastUpdateBy
    }

    fun getItem(
        isNewTask: Boolean = false
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
            lastUpdateBy = lastUpdateBy,
        )
    }

    /**
     * Оставил в этом методе только те действия, которые должнна уметь эта ViewModel
     */
    fun callToRepository(
        action: Action,
        item: TodoItem? = null,
        id: String? = null
    ) = when (action) {
        Action.Add -> {
            viewModelScope.launch(Dispatchers.IO) { repository.addItem(item!!) }
        }
        Action.Delete -> {
            viewModelScope.launch(Dispatchers.IO) { repository.removeItem(item!!) }
        }
        Action.Update -> {
            viewModelScope.launch(Dispatchers.IO) { repository.updateItem(item!!) }
        }
        Action.GetElement -> {
            viewModelScope.launch(Dispatchers.IO) { repository.getItem(id!!) }
        }
        else -> throw IllegalArgumentException()
    }
}