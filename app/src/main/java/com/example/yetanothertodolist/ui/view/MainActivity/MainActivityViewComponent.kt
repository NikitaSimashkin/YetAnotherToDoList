package com.example.yetanothertodolist.ui.view.MainActivity

import com.example.yetanothertodolist.ioc.ApplicationComponent

class MainActivityViewComponent(
    private val activity: MainActivity,
    private val applicationComponent: ApplicationComponent
) {

    private var errorManager: ErrorManager = ErrorManager(applicationComponent.repository, activity)

    init{
        applicationComponent.repository.errorManager = errorManager
    }
}