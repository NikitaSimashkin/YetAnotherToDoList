package com.example.yetanothertodolist.ui.view.MainActivity

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.data.FiveZeroZeroException
import com.example.yetanothertodolist.data.FourZeroFourException
import com.example.yetanothertodolist.data.FourZeroOneException
import com.example.yetanothertodolist.data.FourZeroZeroException
import com.example.yetanothertodolist.di.MainActivityComponentScope
import com.example.yetanothertodolist.other.ConnectiveLiveData
import com.example.yetanothertodolist.other.ErrorManager
import com.example.yetanothertodolist.ui.stateholders.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Класс, который ловит ошибки и отображает их через снэкбар
 * Из за хранения контейнера я отношу его к UI слою, поэтому с репозиторием общаемся через viewModel
 */

@MainActivityComponentScope
class SnackBarErrorManager @Inject constructor(
    private val mainActivity: MainActivity,
    private val viewModel: MainActivityViewModel,
    private val connectiveLiveData: ConnectiveLiveData
) : ErrorManager {

    private val mainActivityContainer: View = mainActivity.findViewById(R.id.main_root)

    init {
        viewModel.setErrorManager(this)
        setInternetChangeListener()
    }

    /**
     * Запускаем на скоупе активити, так как нам надо показать снекбар и повторить действие
     */
    private fun getSnackBar(
        textResId: Int,
        nameActionResId: Int,
        action: suspend () -> Unit
    ) {
        Snackbar.make(mainActivityContainer, textResId, Snackbar.LENGTH_SHORT)
            .setAction(nameActionResId) {
                mainActivity.lifecycleScope.launch {
                    launchWithHandler(action)
                }
            }.show()

    }

    private fun getSnackBarWithoutAction(textResId: Int) {
        Snackbar.make(mainActivityContainer, textResId, Snackbar.LENGTH_SHORT).show()
    }

    override suspend fun launchWithHandler(action: (suspend () -> Unit)?) =
        withContext(Dispatchers.IO) {
            try {
                action!!()
            } catch (e: Exception) {
                when (e) {
                    is FourZeroOneException, is FiveZeroZeroException -> authError(action!!)
                    is FourZeroFourException, is FourZeroZeroException -> notFoundError(action!!)
                    is UnknownHostException -> {
                        /**
                         * Ели нет интернета, нужно просто показать снекбар, этим занимается
                         * метод [setInternetChangeListener]
                         */
                    }
                    else -> unknownError()
                }
            }
        }

    private fun unknownError() {
        getSnackBarWithoutAction(R.string.unknownError)
    }

    private fun notFoundError(action: suspend () -> Unit) {
        viewModel.updateList()
    }

    private fun authError(action: suspend () -> Unit) {
        getSnackBar(R.string.unknownError, R.string.retry, action)
    }

    private fun setInternetChangeListener() {
        connectiveLiveData.observe(mainActivity) {
            if (it) {
                internetTrue()
            } else {
                internetFalse()
            }
        }
        startNoInternetSnackBar()
    }

    private fun internetFalse() {
        if (viewModel.isConnected)
            getSnackBarWithoutAction(R.string.noInternet)
        viewModel.isConnected = false
    }

    private fun internetTrue() {
        if (!viewModel.firstLaunch && !viewModel.isConnected)
            getSnackBarWithoutAction(R.string.yesInternet)
        else
            viewModel.firstLaunch = false

        viewModel.hasConnection()
        viewModel.isConnected = true
    }

    /**
     * Этот метод только для ситуации, когда пользователь запустил приложение без интернета
     * Без этого метода пользователь не узнает, что у него данные не сохраняются на сервер
     */
    private fun startNoInternetSnackBar() {
        if (viewModel.firstLaunch)
            mainActivity.lifecycleScope.launch(Dispatchers.IO) {
                delay(3000)
                if (viewModel.firstLaunch)
                    getSnackBarWithoutAction(R.string.noInternet)
                viewModel.firstLaunch = false
            }
    }


}