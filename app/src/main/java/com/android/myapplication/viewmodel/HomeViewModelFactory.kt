package com.android.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.myapplication.data.HomeRepository
import timber.log.Timber

/**
 * Factory for creating a [GardenPlantingListViewModel] with a constructor that takes a
 * [GardenPlantingRepository].
 */
class HomeViewModelFactory(
    private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.e("HomeViewModelFactory","gen ViewModel:"+modelClass.simpleName)
        return HomeViewModel(repository) as T
    }
}