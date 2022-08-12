package com.example.yetanothertodolist.ui.view.MainActivity

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.data.FiveZeroZeroException
import com.example.yetanothertodolist.data.FourZeroFourException
import com.example.yetanothertodolist.data.FourZeroOneException
import com.example.yetanothertodolist.data.FourZeroZeroException
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.net.UnknownHostException
import kotlin.Exception

/**
 * Класс, который ловит и обрабатывает ошибки
 */
class ErrorManager(
    private val repository: TodoItemRepository,
    private val mainActivity: MainActivity
) {

    private val mainActivityContainer = mainActivity.findViewById<View>(R.id.main_root)

    /**
     * Когда нет интернета, то при любом действии вылазит снекбар, это может раздражать.
     * Этот флаг как раз фиксит это
     */
    var enableNotice: Boolean = true

    /**
     * Запускаем на скоупе активити, так как нам надо показать снекбар и повторить действие
     */
    private fun getSnackBar(
        textResId: Int,
        nameActionResId: Int,
        action: suspend () -> Unit
    ) {
        Snackbar.make(mainActivityContainer!!, textResId, Snackbar.LENGTH_SHORT)
            .setAction(nameActionResId) {
                mainActivity.lifecycleScope.launch {
                    launchWithHandler(action)
                }
            }.show()

    }

    private fun getSnackBarWithoutAction(textResId: Int) {
        Snackbar.make(mainActivityContainer!!, textResId, Snackbar.LENGTH_SHORT).show()
    }

    suspend fun launchWithHandler(action: (suspend () -> Unit)? = null) {
        withContext(Dispatchers.IO) {
            try {
                action!!()
            } catch (e: Exception) {
                if (enableNotice) {
                    when (e) {
                        is FourZeroOneException, is FiveZeroZeroException -> {
                            authError(action!!)
                        }
                        is FourZeroFourException, is FourZeroZeroException -> {
                            notFountError(action!!)
                        }
                        is UnknownHostException -> {
                            internetConnectionError(action!!)
                        }
                        else -> unknownError()
                    }
                }
            }
        }
    }

    private fun unknownError() {
        getSnackBarWithoutAction(R.string.unknownError)
    }

    private fun internetConnectionError(action: suspend () -> Unit) {
        getSnackBar(R.string.noInternet, R.string.retry, action)
    }

    private suspend fun notFountError(action: suspend () -> Unit) {
        repository.updateList()
    }

    private fun authError(action: suspend () -> Unit) {
        getSnackBar(R.string.unknownError, R.string.retry, action)
    }
}