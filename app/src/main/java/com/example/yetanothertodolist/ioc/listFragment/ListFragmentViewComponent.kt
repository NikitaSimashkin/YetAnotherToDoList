package com.example.yetanothertodolist.ioc.listFragment

import androidx.lifecycle.LifecycleOwner
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.example.yetanothertodolist.ui.view.listFragment.ListFragmentViewController

/**
 * Компонент фрагмента листа для вьюшек
 */
class ListFragmentViewComponent(
    binding: ListFragmentBinding,
    viewModel: ListFragmentViewModel,
    listFragmentComponent: ListFragmentComponent,
    lifecycleOwner: LifecycleOwner
) {

    val listFragmentViewController =
        ListFragmentViewController(binding, viewModel, listFragmentComponent.adapter, lifecycleOwner)
}