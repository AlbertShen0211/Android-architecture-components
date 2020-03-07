package com.android.myapplication

import android.app.Application
import android.content.Context
import androidx.work.*
import androidx.work.ExistingPeriodicWorkPolicy.KEEP
import com.android.myapplication.workers.RefreshDataWork
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

open class TodoApplication : Application() {
    companion object {
        var appContext: Context by Delegates.notNull()

    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        appContext = applicationContext
        workManagerJob()
    }


    private fun workManagerJob() {
        val workManagerConfiguration = Configuration.Builder()
            .setWorkerFactory(RefreshDataWork.Factory())
            .build()

        WorkManager.initialize(appContext, workManagerConfiguration)
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        // Specify that the work should attempt to run every day
        val work = PeriodicWorkRequestBuilder<RefreshDataWork>(2, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        // Enqueue it work WorkManager, keeping any previously scheduled jobs for the same
        // work.
        WorkManager.getInstance(appContext)
            .enqueueUniquePeriodicWork(RefreshDataWork::class.java.name, KEEP, work)
    }
}
