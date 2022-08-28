package com.example.yetanothertodolist.ui.stateholders

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.util.ConnectiveLiveData
import com.example.yetanothertodolist.util.ErrorManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val repository: TodoItemRepository,
    private val connectiveLiveData: ConnectiveLiveData
) : ViewModel() {

    private val observer: Observer<Boolean> =
        Observer<Boolean> {
            if (it) updateList()
        }

    init {
        setInternetListener()
        updateList()
    }

    private fun setInternetListener() {
        connectiveLiveData.observeForever(observer)
    }

    override fun onCleared() {
        super.onCleared()
        connectiveLiveData.removeObserver(observer)
    }

    var listIsNotReceived = true

    fun setErrorManager(em: ErrorManager) {
        repository.errorManager = em
    }

    var isConnectedForSnackBarErrorManager = false

    fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateList()
        }
    }
}