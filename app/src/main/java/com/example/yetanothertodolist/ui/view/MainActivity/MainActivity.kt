package com.example.yetanothertodolist.ui.view.MainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.YetAnotherApplication

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
        supportActionBar?.hide()
        mainActivityComponent.errorManager()
    }
}