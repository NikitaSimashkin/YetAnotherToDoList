package com.example.yetanothertodolist.other

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.yetanothertodolist.YetAnotherApplication

/**
 * Раз в 8 часов ходит в сеть и просит список
 */
class UpdateListWorker(val context: Context, workerParams: WorkerParameters) : CoroutineWorker(
    context,
    workerParams
) {
    override suspend fun doWork(): Result {
        val app = (context.applicationContext as YetAnotherApplication)
       // val repository = app.applicationComponent.repository
       // repository.getList()
        return Result.success()
    }
}