package com.example.yetanothertodolist

import android.app.Application
import com.example.yetanothertodolist.ioc.ApplicationComponent

/**
 * Класс приложения, создан лишь для хранения компонента
 */
class YetAnotherApplication : Application() {

    val applicationComponent by lazy { ApplicationComponent(applicationContext) }

}