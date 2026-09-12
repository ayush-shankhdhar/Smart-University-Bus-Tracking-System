package com.example.bustracking.data.repository

import com.example.bustracking.data.local.dao.FavouriteDao
import com.example.bustracking.data.local.entity.FavouriteEntity
import kotlinx.coroutines.flow.Flow

class FavouriteRepository(private val favouriteDao: FavouriteDao) {

    val allFavouritesFlow: Flow<List<FavouriteEntity>> = favouriteDao.getAllFavouritesFlow()

    fun isFavouriteFlow(busName: String): Flow<Boolean> = favouriteDao.isFavouriteFlow(busName)

    suspend fun toggleFavourite(busName: String, routeName: String, driverName: String): Boolean {
        val isFav = favouriteDao.isFavourite(busName)
        if (isFav) {
            favouriteDao.removeFavourite(busName)
            return false
        } else {
            favouriteDao.addFavourite(FavouriteEntity(busName, routeName, driverName))
            return true
        }
    }
}
