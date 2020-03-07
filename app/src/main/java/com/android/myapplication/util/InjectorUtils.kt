package com.android.myapplication.util

import android.content.Context
import com.android.myapplication.data.AppDatabase
import com.android.myapplication.data.HomeRepository
import com.android.myapplication.network.Api
import com.android.myapplication.viewmodel.HomeViewModelFactory
import timber.log.Timber

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private val TAG: String=InjectorUtils::class.java.simpleName

     fun getHomeRepository(context: Context): HomeRepository {
        Timber.e(TAG,"getHomeRepository")
        return HomeRepository.getInstance(
            Api.retrofitService,
            AppDatabase.getInstance(context.applicationContext).gardenPlantingDao())
    }

    fun provideHomeViewModelFactory(
        context: Context
    ): HomeViewModelFactory {
        Timber.e(TAG,"provideHomeViewModelFactory")
        val repository = getHomeRepository(context)
        return HomeViewModelFactory(repository)
    }
}
