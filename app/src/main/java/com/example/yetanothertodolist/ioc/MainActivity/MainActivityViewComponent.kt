package com.example.yetanothertodolist.ioc.MainActivity

import com.example.yetanothertodolist.ioc.ApplicationComponent
import com.example.yetanothertodolist.other.ErrorManager
import com.example.yetanothertodolist.ui.stateholders.MainActivityViewModel
import com.example.yetanothertodolist.ui.view.MainActivity.MainActivity
import com.example.yetanothertodolist.ui.view.MainActivity.SnackBarErrorManager

class MainActivityViewComponent(
    private val activity: MainActivity,
    private val applicationComponent: ApplicationComponent,
    private val viewModel: MainActivityViewModel
) {

    private val errorManager: ErrorManager = SnackBarErrorManager(activity, viewModel)
}