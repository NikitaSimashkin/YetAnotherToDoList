package com.example.yetanothertodolist.ioc.listFragment

import android.content.Context
import com.example.yetanothertodolist.ioc.ApplicationComponent
import com.example.yetanothertodolist.other.ConnectiveLiveData
import com.example.yetanothertodolist.ui.stateholders.Action
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.example.yetanothertodolist.ui.view.listFragment.ListFragment
import com.example.yetanothertodolist.ui.view.listFragment.TodoAdapter

/**
 * Компонент фрагмента листа, хранит адаптер и устанавливает слушатель на изменение интернета
 */
class ListFragmentComponent(
    context: Context,
    private val viewModel: ListFragmentViewModel,
    private val fragment: ListFragment,
    private val applicationComponent: ApplicationComponent
) {
    val adapter = TodoAdapter(viewModel, context)

    init {
        setInternetChangeListener(context)
    }

    private var first = true
    private fun setInternetChangeListener(context: Context) {
        ConnectiveLiveData(context).observe(fragment.requireActivity()) {
            if (it) {
                if (first){
                    viewModel.callToRepository(Action.GetList)
                    first = false
                } else
                    viewModel.callToRepository(Action.UpdateList)
            } else {
                // не придумал что тут делать
            }
        }
    }
}