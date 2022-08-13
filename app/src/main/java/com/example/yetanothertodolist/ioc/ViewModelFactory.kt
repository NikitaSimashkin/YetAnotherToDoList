package com.example.yetanothertodolist.ioc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.ui.stateholders.AddFragmentViewModel
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.example.yetanothertodolist.ui.stateholders.MainActivityViewModel

/**
 * Фабрика для создания viewModel, передает обоим viewModel репозиторий
 */
class ViewModelFactory(
    val repository: TodoItemRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        ListFragmentViewModel::class.java -> ListFragmentViewModel(
            repository
        )
        AddFragmentViewModel::class.java -> AddFragmentViewModel(
            repository
        )
        MainActivityViewModel::class.java -> MainActivityViewModel(
            repository
        )
        else -> throw IllegalArgumentException("${modelClass.simpleName} cannot be provided.")
    } as T
}