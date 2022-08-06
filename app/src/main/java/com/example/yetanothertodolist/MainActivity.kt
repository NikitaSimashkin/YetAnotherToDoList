package com.example.yetanothertodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var scope: CoroutineScope
            private set

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        scope = lifecycleScope
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}