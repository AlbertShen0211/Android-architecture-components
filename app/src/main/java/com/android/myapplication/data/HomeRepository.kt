package com.android.myapplication.data
import com.android.myapplication.network.MainNetwork
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class HomeRepository private constructor(
    val network: MainNetwork,
    private val gardenPlantingDao: PlantDao
) {
    suspend fun createGardenPlanting(plantName: String) {
        val gardenPlanting = Plant(plantName)
        gardenPlantingDao.insertPlant(gardenPlanting)
    }

    suspend fun removeGardenPlanting(gardenPlanting: Plant) {
        gardenPlantingDao.deletePlant(gardenPlanting)
    }


    fun getGardenPlantings() = gardenPlantingDao.getGardenPlantings()


    suspend fun refreshView(): String {
        try {
            val result = withTimeout(50_00) {
               network.fetchNextTitle()
            }
            createGardenPlanting(result)
            return result
        } catch (error: Throwable) {
            throw RefreshError("Unable to refresh title", error)
        }
    }

    suspend fun getBanner(): Response<List<Banner>>{
        try {

            return withTimeout(50_00) {
                Timber.e("respo getBanner")
                network.getBanner()
            }
        } catch (error: Throwable) {
            Timber.e( "err-- "+error.message)
            throw RefreshError("Unable to refresh title ->", error)
        }
    }


    class RefreshError(message: String, cause: Throwable) : Throwable(message, cause)


    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: HomeRepository? = null

        fun getInstance(network: MainNetwork, gardenPlantingDao: PlantDao) =
            instance ?: synchronized(this) {
                instance ?: HomeRepository(network, gardenPlantingDao).also { instance = it }
            }
    }


}