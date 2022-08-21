package com.example.yetanothertodolist.ui.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.util.ErrorManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: TodoItemRepository) : ViewModel() {

    var listIsNotReceived = true

    fun setErrorManager(em: ErrorManager) {
        repository.errorManager = em
    }

    /**
     *  [firstLaunch] [listIsNotReceived] [isConnected] нужны для корректного отображения снэкбаров
     *  Благодаря этим полям есть возможность учесть все частные случаи
     */
    var isConnected = false

    var firstLaunch = true

    fun hasConnection() {
        updateList()
    }

    fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateList()
        }
    }
}