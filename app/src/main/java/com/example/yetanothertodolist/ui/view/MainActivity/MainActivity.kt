package com.example.yetanothertodolist.ui.view.MainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.YetAnotherApplication
import com.example.yetanothertodolist.other.ConstValues

/**
 * Главное и единственное activity, на котором отобраюажтся фрагменты
 */
class MainActivity : AppCompatActivity() {

    val mainActivityComponent by lazy {
        (application as YetAnotherApplication).applicationComponent.mainActivityComponent
            .create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initStartSettings()
        supportActionBar?.hide()
    }

    private fun initStartSettings() {
        mainActivityComponent.errorManager()
        when (mainActivityComponent.sharedPreference().getString(ConstValues.START_THEME, "")!!) {
            "dark" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mainActivityComponent.sharedPreference()
                    .edit { putString(ConstValues.START_THEME, "dark") }
            }
            "light" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                mainActivityComponent.sharedPreference()
                    .edit { putString(ConstValues.START_THEME, "light") }
            }
            "system" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                mainActivityComponent.sharedPreference()
                    .edit { putString(ConstValues.START_THEME, "system") }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as YetAnotherApplication).applicationComponent.repository.errorManager = null
    }
}