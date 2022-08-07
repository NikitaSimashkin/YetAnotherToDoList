package com.example.yetanothertodolist.Backend

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.yetanothertodolist.TodoItemRepository
import com.example.yetanothertodolist.YetAnotherApplication


class UpdateListWorker(val context: Context, workerParams: WorkerParameters) : CoroutineWorker(context,
    workerParams
) {
    override suspend fun doWork(): Result {
        val app = (context.applicationContext as YetAnotherApplication)
        val repository = app.repository
        repository.getServerList()
        return Result.success()
    }
}