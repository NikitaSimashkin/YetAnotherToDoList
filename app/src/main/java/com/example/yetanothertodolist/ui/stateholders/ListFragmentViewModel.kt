package com.example.yetanothertodolist.ui.stateholders

import androidx.lifecycle.*
import com.example.yetanothertodolist.YetAnotherApplication
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.util.ConnectiveLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * viewModel для работы с репозиторием
 */
class ListFragmentViewModel(
    private val repository: TodoItemRepository,
    private val connectiveLiveData: ConnectiveLiveData
) : ViewModel() {

    val tasks = repository.tasks

    private val observer: Observer<Boolean> =
        Observer<Boolean> { value -> isConnected = value }
    private var isConnected: Boolean = connectiveLiveData.value ?: false

    init {
        connectiveLiveData.observeForever(observer)
    }

    override fun onCleared() {
        super.onCleared()
        connectiveLiveData.removeObserver(observer)
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
    }

    fun changeCheckBox(item: TodoItem, done: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItem(
                item.copy(
                    done = done,
                    changedAt = LocalDateTime.now(),
                    lastUpdateBy = YetAnotherApplication.deviceId,
                    isDeleted = false
                )
            )
        }
    }

    private val _eyeButton = MutableLiveData(true)
    val eyeButton: LiveData<Boolean> = _eyeButton

    fun changeEye() {
        _eyeButton.value = _eyeButton.value!!.not()
    }

    fun getListToAdapter(): List<TodoItem> {
        return if (eyeButton.value!!)
            tasks.value!!
        else
            tasks.value!!.filter { !it.done }
    }

    val getDoneTasks
        get() = tasks.value!!.count { it.done }

    var scrollPosition = 0
    var appBarOffset = 0
}