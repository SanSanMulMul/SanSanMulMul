package com.sansantek.sansanmulmul.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sansantek.sansanmulmul.BuildConfig
import com.sansantek.sansanmulmul.data.local.dao.StepCounterDao
import com.sansantek.sansanmulmul.data.local.entity.StepCount

private const val DATABASE_NAME = BuildConfig.STEP_COUNTER_DATABASE_DB

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
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

//        private val MIGRATION_1_2 = object : Migration(1,2){
//            override fun migrate(db: SupportSQLiteDatabase) {
//                // Step 1: 임시 테이블 생성
//                db.execSQL("" +
//                        "CREATE " +
//                        "TABLE new_YourEntity " +
//                        "(year INTEGER NOT NULL, month INTEGER NOT NULL, day INTEGER NOT NULL, stepCount INTEGER NOT NULL, " +
//                        "PRIMARY KEY(year, month, day))"
//                )
//
//                // Step 2: 기존 테이블의 데이터를 임시 테이블로 옮기면서 stepCount를 합산
//                db.execSQL("INSERT INTO new_YourEntity (year, month, day, stepCount) " +
//                        "SELECT year, month, day, SUM(stepCount) as stepCount " +
//                        "FROM StepCounter " +
//                        "GROUP BY year, month, day")
//
//                // Step 3: 기존 테이블 삭제
//                db.execSQL("DROP TABLE StepCounter")
//
//                db.execSQL("ALTER TABLE new_YourEntity RENAME TO StepCounter")
//            }
//        }

    }
}