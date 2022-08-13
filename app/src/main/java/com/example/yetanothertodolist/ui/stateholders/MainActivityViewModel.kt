package com.example.yetanothertodolist.ui.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.other.ErrorManager
import com.example.yetanothertodolist.ui.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: TodoItemRepository): ViewModel() {
    /**
     * При первом заходе список скачивается, дальше уже только добавляется на сервер через patch
     * Если мы перевернем экран, нам надо все равно помнить, что мы уже скачивали список
     *
     * Немного странное будет поведение, когда пользователь зашел без интернета и начал
     * добавлять задания, по сути все что он сделает - пропадет как только появится интернет, эта
     * проблема решается через локальную базу данных, которую мы пока не проходили
     */
    var firstLaunch = true


    fun callToRepository(
        action: Action
    ) {
        when (action) {
            Action.GetList -> { viewModelScope.launch(Dispatchers.IO) { repository.getList()}}
            Action.UpdateList -> { viewModelScope.launch(Dispatchers.IO) { repository.updateList() } }
            else -> throw IllegalArgumentException()
        }
    }

    fun setErrorManager(em: ErrorManager){
        repository.errorManager = em
    }
}