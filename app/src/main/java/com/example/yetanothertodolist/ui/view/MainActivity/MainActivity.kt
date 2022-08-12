package com.example.yetanothertodolist.ui.view.MainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.YetAnotherApplication

/**
 * Главное и единственное activity, на котором отобраюажтся фрагменты
 */
class MainActivity : AppCompatActivity() {

    private var mainActivityViewComponent: MainActivityViewComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        mainActivityViewComponent = MainActivityViewComponent(
            this,
            (application as YetAnotherApplication).applicationComponent
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivityViewComponent = null
    }
}