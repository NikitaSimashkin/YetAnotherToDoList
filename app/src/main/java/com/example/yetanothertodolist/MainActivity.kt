package com.example.yetanothertodolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.yetanothertodolist.Backend.ConnectiveLiveData
import com.example.yetanothertodolist.Backend.UpdateListWorker
import com.example.yetanothertodolist.Fragments.AddFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var scope: CoroutineScope
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val updateListRequest = PeriodicWorkRequestBuilder<UpdateListWorker>(8, TimeUnit.HOURS).build()
        WorkManager.getInstance(applicationContext).enqueue(updateListRequest)
        // плодишь кучу воркеров, используй enqueueUniqueWork

        snackBarStringsInit()


        val container = findViewById<ConstraintLayout>(R.id.main_root)
        ConnectiveLiveData(applicationContext).observe(this){
            if (it) {
                // условие нужно для того, чтобы при запуске приложения без интернета patch не стер все
                // разве revision от этого не спасает?
                // должен спасать, в пустом списке нет ничего уникального, он может получиться не только на первом старте.
                // тут потенциальный баг
                if ((application as YetAnotherApplication).repository.tasks.value?.isEmpty() == true)
                    getList()
                else {
                    patch()
                }
                Snackbar.make(container, resources.getString(R.string.yesInternet), Snackbar.LENGTH_SHORT).show()
            }
            else {
                Snackbar.make(container, resources.getString(R.string.noInternet), Snackbar.LENGTH_SHORT).show()
            }
        }

        scope = lifecycleScope // нигде не зануляется, утечка памяти
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun patch(){
        lifecycleScope.launch(Dispatchers.IO) {
            (application as YetAnotherApplication).repository.updateServerList()
        }
    }

    private fun getList(){
        lifecycleScope.launch(Dispatchers.IO) {
            (application as YetAnotherApplication).repository.getServerList()
        }
    }

    private fun snackBarStringsInit(){ // зачем это всё, если снекбары умеют принимать res_id вместо строк?
        AddFragment.retry = resources.getText(R.string.retry).toString()
        AddFragment.revisionError = resources.getText(R.string.revisionError).toString()
        AddFragment.elementNotFount = resources.getText(R.string.elementNotFoundError).toString()
        AddFragment.unknownError = resources.getText(R.string.unknownError).toString()
        AddFragment.later = resources.getText(R.string.later).toString()
        AddFragment.noInternet = resources.getString(R.string.noInternet)
    }
}
