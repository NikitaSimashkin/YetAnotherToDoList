package com.example.yetanothertodolist.ui.view.MainActivity

import android.content.Context
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.data.FiveZeroZeroException
import com.example.yetanothertodolist.data.FourZeroFourException
import com.example.yetanothertodolist.data.FourZeroOneException
import com.example.yetanothertodolist.data.FourZeroZeroException
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.other.ConnectiveLiveData
import com.example.yetanothertodolist.other.ErrorManager
import com.example.yetanothertodolist.ui.stateholders.Action
import com.example.yetanothertodolist.ui.stateholders.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.net.UnknownHostException
import kotlin.Exception

/**
 * Класс, который ловит ошибки и отображает их через снэкбар
 * Из за хранения контейнера я отношу его к UI слою, поэтому с репозиторием общаемся через viewModel
 */
class SnackBarErrorManager(
    private val mainActivity: MainActivity,
    private val viewModel: MainActivityViewModel
) : ErrorManager {

    init {
        viewModel.setErrorManager(this)
        setInternetChangeListener(mainActivity.applicationContext)
    }

    private val mainActivityContainer = mainActivity.findViewById<View>(R.id.main_root)

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

    override suspend fun launchWithHandler(action: (suspend () -> Unit)?) =
        withContext(Dispatchers.IO) {
            try {
                action!!()
            } catch (e: Exception) {
                when (e) {
                    is FourZeroOneException, is FiveZeroZeroException -> authError(action!!)
                    is FourZeroFourException, is FourZeroZeroException -> notFountError(action!!)
                    is UnknownHostException -> internetConnectionError(action!!)
                    else -> unknownError()
                }
            }
        }

    private fun unknownError() {
        getSnackBarWithoutAction(R.string.unknownError)
    }

    /**
     * Отсутствие интернета не обрабатываем, этим занимается [setInternetChangeListener]
     */
    private fun internetConnectionError(action: suspend () -> Unit) {

    }

    private fun notFountError(action: suspend () -> Unit) {
        viewModel.callToRepository(Action.UpdateList)
    }

    private fun authError(action: suspend () -> Unit) {
        getSnackBar(R.string.unknownError, R.string.retry, action)
    }

    private fun setInternetChangeListener(context: Context) {
        ConnectiveLiveData(context).observe(mainActivity) {
            if (it) {
                if (viewModel.firstLaunch) {
                    viewModel.callToRepository(Action.GetList)
                    viewModel.firstLaunch = false
                } else {
                    viewModel.callToRepository(Action.UpdateList)
                    getSnackBarWithoutAction(R.string.yesInternet)
                }
            } else {
                getSnackBarWithoutAction(R.string.noInternet)
            }
        }
    }
}