package com.example.yetanothertodolist.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.YetAnotherApplication
import com.example.yetanothertodolist.ioc.MainActivityComponent
import com.example.yetanothertodolist.other.ErrorManager
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel

/**
 * Главное и единственное activity, на котором отобраюажтся фрагменты
 */
class MainActivity : AppCompatActivity() {

    private var component: MainActivityComponent? = null
    private val viewModel: ListFragmentViewModel by viewModels {
        (application as YetAnotherApplication).applicationComponent.viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        component = MainActivityComponent(
            (application as YetAnotherApplication).applicationComponent.errorManager,
            findViewById(R.id.main_root), lifecycleScope, viewModel
        )
        component!!.setUpErrorManager()
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
    }
}