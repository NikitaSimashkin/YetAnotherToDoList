package com.example.yetanothertodolist.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerCreator constructor(context: Context) {

    init {
        val updateListRequest =
            PeriodicWorkRequestBuilder<UpdateListWorker>(8, TimeUnit.HOURS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "UpdateList",
            ExistingPeriodicWorkPolicy.KEEP,
            updateListRequest
        )
    }
}