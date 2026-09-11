package com.example.bustracking.data.local.dao

import androidx.room.*
import com.example.bustracking.data.local.entity.FavouriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourites ORDER BY addedAt DESC")
    fun getAllFavouritesFlow(): Flow<List<FavouriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE busName = :busName LIMIT 1)")
    fun isFavouriteFlow(busName: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE busName = :busName LIMIT 1)")
    suspend fun isFavourite(busName: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavourite(favourite: FavouriteEntity)

    @Query("DELETE FROM favourites WHERE busName = :busName")
    suspend fun removeFavourite(busName: String): Int
}
