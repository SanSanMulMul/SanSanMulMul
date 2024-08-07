package com.sansantek.sansanmulmul.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sansantek.sansanmulmul.BuildConfig
import com.sansantek.sansanmulmul.data.local.dao.StepCounterDao
import com.sansantek.sansanmulmul.data.local.entity.StepCount

private const val DATABASE_NAME = BuildConfig.DATABASE_NAME

@Database(entities = [StepCount::class], version = 1)
abstract class StepCountDatabase : RoomDatabase() {
    abstract fun stepCounterDao(): StepCounterDao

    companion object {
        @Volatile
        private var INSTANCE: StepCountDatabase? = null

        fun getDatabase(
            context: Context
        ): StepCountDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StepCountDatabase::class.java,
                    DATABASE_NAME
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}