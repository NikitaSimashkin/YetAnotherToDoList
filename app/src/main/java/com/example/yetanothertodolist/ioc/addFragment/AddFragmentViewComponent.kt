package com.example.yetanothertodolist.ioc.addFragment

import com.example.yetanothertodolist.databinding.AddFragmentBinding
import com.example.yetanothertodolist.ui.stateholders.AddFragmentViewModel
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.example.yetanothertodolist.ui.view.addFragment.AddFragmentViewController

/**
 * Компонент фрагмента добавления для вьюшек
 */
class AddFragmentViewComponent(
    binding: AddFragmentBinding,
    addModel: AddFragmentViewModel,
    addFragmentComponent: AddFragmentComponent
) {
    val addFragmentViewController =
        AddFragmentViewController(binding, addModel, addFragmentComponent.importanceAdapter)
}