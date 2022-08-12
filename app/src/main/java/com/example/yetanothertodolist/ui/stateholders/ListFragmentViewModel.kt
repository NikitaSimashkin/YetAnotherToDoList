package com.example.yetanothertodolist.ui.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yetanothertodolist.data.FiveZeroZeroException
import com.example.yetanothertodolist.data.FourZeroFourException
import com.example.yetanothertodolist.data.FourZeroOneException
import com.example.yetanothertodolist.data.FourZeroZeroException
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.ui.model.TodoItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

/**
 * viewModel для работы с репозиторием
 */
class ListFragmentViewModel(private val repository: TodoItemRepository) : ViewModel() {

    val tasks = repository.tasks

    /**
     * Оставил в этом методе только те действия, которые должна уметь эта ViewModel
     */
    fun callToRepository(
        action: Action,
        item: TodoItem? = null,
        id: String? = null
    ) {
        when (action) {
            Action.Update -> { viewModelScope.launch(Dispatchers.IO) { repository.updateItem(item!!) } }
            Action.GetList -> { viewModelScope.launch(Dispatchers.IO) { repository.getList()}}
            Action.UpdateList -> { viewModelScope.launch(Dispatchers.IO) { repository.updateList() } }
            else -> throw IllegalArgumentException()
        }
    }
}