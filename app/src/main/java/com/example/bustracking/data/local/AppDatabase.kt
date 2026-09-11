package com.example.bustracking.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bustracking.data.local.dao.*
import com.example.bustracking.data.local.entity.*

@Database(
    entities = [
        BusEntity::class,
        RouteEntity::class,
        StopEntity::class,
        ScheduleEntity::class,
        FavouriteEntity::class,
        ComplaintEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun busDao(): BusDao
    abstract fun routeDao(): RouteDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun favouriteDao(): FavouriteDao
    abstract fun complaintDao(): ComplaintDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "campusride_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
