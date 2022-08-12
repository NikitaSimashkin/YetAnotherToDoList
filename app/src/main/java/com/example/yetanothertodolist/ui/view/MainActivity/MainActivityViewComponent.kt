package com.example.yetanothertodolist.ui.view.MainActivity

import com.example.yetanothertodolist.ioc.ApplicationComponent
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel

class MainActivityViewComponent(
    private val activity: MainActivity,
    private val applicationComponent: ApplicationComponent,
    private val viewModel: ListFragmentViewModel
) {

    private var errorManager: ErrorManager = ErrorManager(applicationComponent.repository, activity, viewModel)
}