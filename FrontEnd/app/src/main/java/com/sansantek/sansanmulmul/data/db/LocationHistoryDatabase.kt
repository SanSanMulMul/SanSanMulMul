package com.sansantek.sansanmulmul.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sansantek.sansanmulmul.BuildConfig
import com.sansantek.sansanmulmul.BuildConfig.STEP_COUNTER_DATABASE_DB
import com.sansantek.sansanmulmul.data.local.dao.LocationHistoryDao
import com.sansantek.sansanmulmul.data.local.entity.LocationHistory

private const val DATABASE_NAME = BuildConfig.LOCATION_HISTORY_DATABASE_DB

@Database(entities = [LocationHistory::class], version = 1)
abstract class LocationHistoryDatabase : RoomDatabase() {
    abstract fun locationHistoryDao(): LocationHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: LocationHistoryDatabase? = null

        fun getDatabase(
            context: Context
        ): LocationHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationHistoryDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}