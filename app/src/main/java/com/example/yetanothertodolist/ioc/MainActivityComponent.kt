package com.example.yetanothertodolist.ioc

import android.view.View
import com.example.yetanothertodolist.other.ErrorManager
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import kotlinx.coroutines.CoroutineScope

/**
 * На данный момент этот класс ничего не создает, а только "заполняет" Error Manager
 */
class MainActivityComponent(
    private val em: ErrorManager,
    private val container: View,
    private val scope: CoroutineScope,
    private val viewModel: ListFragmentViewModel
) {

    fun setUpErrorManager(){
        em.mainActivityContainer = container
        em.scope = scope
        em.viewModel = viewModel
    }
}