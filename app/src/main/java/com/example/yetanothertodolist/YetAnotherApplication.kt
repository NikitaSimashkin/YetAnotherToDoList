package com.example.yetanothertodolist

import android.app.Application
import com.example.yetanothertodolist.di.ApplicationComponent
import com.example.yetanothertodolist.di.DaggerApplicationComponent

/**
 * Класс приложения, создан лишь для хранения компонента
 */
class YetAnotherApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
        applicationComponent.worker
    }

}