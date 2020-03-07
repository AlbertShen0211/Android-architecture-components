package com.android.myapplication.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.android.myapplication.util.InjectorUtils

/**
 * Worker job to refresh titles from the network while the app is in the background.
 *
 * WorkManager is a library used to enqueue work that is guaranteed to execute after its constraints
 * are met. It can run work even when the app is in the background, or not running.
 */
class RefreshDataWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {

            var repository = InjectorUtils.getHomeRepository(applicationContext)
            repository.refreshView()
            Result.success()
        } catch (error: Error) {
            Result.failure()
        }
    }

    class Factory() : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker? {

            return RefreshDataWork(appContext, workerParameters)
        }

    }
}