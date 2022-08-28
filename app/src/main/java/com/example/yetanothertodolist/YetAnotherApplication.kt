package com.example.yetanothertodolist

import android.app.Application
import com.example.yetanothertodolist.di.ApplicationComponent
import com.example.yetanothertodolist.di.DaggerApplicationComponent
import com.example.yetanothertodolist.other.ConstValues
import java.util.*

/**
 * Класс приложения, создан лишь для хранения компонента
 */
open class YetAnotherApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    companion object {
        var deviceId = ""
            private set
    }

    open val url = "https://beta.mrdekk.ru/todobackend/"

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
        initWorker()
        setDeviceId()

    }

    private fun setDeviceId() {
        val sharedPreference = getSharedPreferences(ConstValues.SHARED_PREF_FILE_NAME, MODE_PRIVATE)

        if (sharedPreference.getString(ConstValues.DEVICE_ID, "") == "") {
            val id = UUID.randomUUID().toString()
            deviceId = id
            sharedPreference.edit().putString(ConstValues.DEVICE_ID, id).apply()
        } else {
            deviceId = sharedPreference.getString(ConstValues.DEVICE_ID, "")!!
        }
    }

    private fun initWorker() {
        applicationComponent.worker
    }

}