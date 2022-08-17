package com.example.yetanothertodolist.ui.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.other.ErrorManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: TodoItemRepository) : ViewModel() {
    /**
     * При первом заходе список скачивается, дальше уже только добавляется на сервер через patch
     * Если мы перевернем экран, нам надо все равно помнить, что мы уже скачивали список
     *
     * Немного странное будет поведение, когда пользователь зашел без интернета и начал
     * добавлять задания, по сути все что он сделает - пропадет как только появится интернет, эта
     * проблема решается через локальную базу данных, которую мы пока не проходили
     * Нет, бд не должна решать эту проблему, бд должна решать только проблему "пользователь поменял
     * что-то без интернета и закрыл приложение"
     */
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
        if (listIsNotReceived)
            viewModelScope.launch(Dispatchers.IO) {
                repository.getList()
                listIsNotReceived = false
            }
        else
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateList()
            }
    }

    fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateList()
        }
    }
}
