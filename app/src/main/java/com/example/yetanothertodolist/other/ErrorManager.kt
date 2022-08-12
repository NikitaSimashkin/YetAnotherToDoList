package com.example.yetanothertodolist.other

import android.view.View
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.data.FiveZeroZeroException
import com.example.yetanothertodolist.data.FourZeroFourException
import com.example.yetanothertodolist.data.FourZeroOneException
import com.example.yetanothertodolist.data.FourZeroZeroException
import com.example.yetanothertodolist.ui.stateholders.Action
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.net.UnknownHostException
import kotlin.Exception

/**
 * Класс, который ловит и обрабатывает ошибки
 */
class ErrorManager {

    var mainActivityContainer: View? = null
    var scope: CoroutineScope? = null
    var viewModel: ListFragmentViewModel? = null

    private fun  getSnackBar(
        textResId: Int,
        nameActionResId: Int,
        action: suspend () -> Unit
    ) {
        Snackbar.make(mainActivityContainer!!, textResId, Snackbar.LENGTH_SHORT)
            .setAction(nameActionResId){
                scope!!.launch {
                    launchWithHandler(action)
                }
            }.show()
    }

    private fun getSnackBarWithoutAction(textResId: Int){
        Snackbar.make(mainActivityContainer!!, textResId, Snackbar.LENGTH_SHORT).show()
    }

    suspend fun launchWithHandler(action: (suspend () -> Unit)? = null){
        withContext(Dispatchers.IO) {
            try {
                action!!()
            } catch (e: Exception){
                when (e) {
                    is FourZeroZeroException -> { revisionError(action!!)}
                    is FourZeroOneException -> {authError(action!!)}
                    is FourZeroFourException -> {notFountError(action!!)}
                    is FiveZeroZeroException -> {serverError(action!!)}
                    is UnknownHostException -> {internetConnectionError(action!!)}
                    else -> {unknownError()}
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

    private fun serverError(action: suspend () -> Unit) {
        getSnackBar(R.string.unknownError, R.string.retry, action)
    }

    private fun notFountError(action: suspend () -> Unit) {
       // getSnackBar(R.string.elementNotFoundError, R.string.retry, action)
        viewModel!!.callToRepository(Action.UpdateList)
    }

    private fun authError(action: suspend () -> Unit) {
        getSnackBar(R.string.unknownError, R.string.retry, action)
    }

    private fun revisionError(action: suspend () -> Unit) {
       // getSnackBar(R.string.revisionError, R.string.retry, action)
        viewModel!!.callToRepository(Action.UpdateList)
    }
}