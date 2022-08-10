package com.example.yetanothertodolist.Backend

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.yetanothertodolist.YetAnotherApplication


class UpdateListWorker(val context: Context, workerParams: WorkerParameters) : CoroutineWorker(context,
    workerParams
) {
    override suspend fun doWork(): Result {
        val app = (context.applicationContext as YetAnotherApplication)
        val repository = app.repository
        repository.getServerList() // по-хорошему нужно перейти на Dispatchers.IO,
        // но по-факту это скорее всего ничего не поменяет, оно и так на Dispatchers.Default
        return Result.success()
    }
}
