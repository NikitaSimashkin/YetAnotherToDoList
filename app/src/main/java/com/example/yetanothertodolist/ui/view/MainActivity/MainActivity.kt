package com.example.yetanothertodolist.ui.view.MainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.YetAnotherApplication
import com.example.yetanothertodolist.ioc.MainActivity.MainActivityViewComponent
import com.example.yetanothertodolist.ui.stateholders.MainActivityViewModel

/**
 * Главное и единственное activity, на котором отобраюажтся фрагменты
 */
class MainActivity : AppCompatActivity() {

    private var mainActivityViewComponent: MainActivityViewComponent? = null
    private val viewModel: MainActivityViewModel by viewModels {
        (application as YetAnotherApplication).applicationComponent.viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        mainActivityViewComponent = MainActivityViewComponent(
            this,
            (application as YetAnotherApplication).applicationComponent,
            viewModel
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivityViewComponent = null
    }
}