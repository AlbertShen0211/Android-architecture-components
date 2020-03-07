package com.android.myapplication.viewmodel

import android.widget.Toast
import androidx.lifecycle.*
import com.android.myapplication.data.Plant
import com.android.myapplication.data.HomeRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel internal constructor(private val homeRepository: HomeRepository) : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val response: LiveData<String> get() = _response

    // The internal MutableLiveData String that stores the most recent response
    private val _err = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val err: LiveData<String> get() = _err


    val gardenPlantings: LiveData<List<Plant>> =
        homeRepository.getGardenPlantings()

    var plantName = gardenPlantings.map {
        if (0 < it.size)
            it.takeLast(1)[0].plantName
        else null

    }

    fun onHomeViewClicked() {
        refreshView()
    }


    /* private val   result:LiveData<String>=launchDataLoad {
         val result:LiveData<String>=  homeRepository.refreshView()
     }*/
    private fun refreshView() = launchDataLoad {
        val result: String = homeRepository.refreshView()
        _response.value = result
    }


    private fun launchDataLoad(block: suspend () -> Unit): Unit {
        viewModelScope.launch {
            try {
                // _spinner.value = true
                block()
            } catch (error: HomeRepository.RefreshError) {
                Timber.e("vm  ", error.message)
                _err.value = error.message
            } finally {
                //  _spinner.value = false
            }
        }


    }
}