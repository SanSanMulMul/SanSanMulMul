package com.sansantek.sansanmulmul.data.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sansantek.sansanmulmul.BuildConfig
import com.sansantek.sansanmulmul.data.local.dao.LocationHistoryDao
import com.sansantek.sansanmulmul.data.local.entity.LocationHistory

private const val DATABASE_NAME = BuildConfig.LOCATION_HISTORY_DATABASE_DB

@Database(entities = [LocationHistory::class], version = 2)
abstract class LocationHistoryDatabase : RoomDatabase() {
    abstract fun locationHistoryDao(): LocationHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: LocationHistoryDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Step 1: 새로운 테이블 생성
                // Step 1: 새로운 테이블 생성
                db.execSQL("""
            CREATE TABLE IF NOT EXISTS LocationHistory_new (
                crewId INTEGER NOT NULL,
                status TEXT NOT NULL,
                time TEXT NOT NULL,
                latitude REAL NOT NULL,
                longitude REAL NOT NULL,
                altitude REAL NOT NULL,
                PRIMARY KEY(crewId, status, time)
            )
        """.trimIndent())

                // Step 2: 기존 데이터를 새로운 테이블로 이동하면서 "상행" 상태로 기본 삽입
                db.execSQL("""
            INSERT INTO LocationHistory_new (crewId, status, time, latitude, longitude, altitude)
            SELECT crewId, '상행', time, latitude, longitude, altitude
            FROM LocationHistory
        """.trimIndent())

                // Step 3: crewId별로 절반의 데이터를 "하행"으로 업데이트
                val cursor = db.query("SELECT DISTINCT crewId FROM LocationHistory_new")
                while (cursor.moveToNext()) {
                    val crewId = cursor.getInt(0)
                    val countCursor = db.query("SELECT COUNT(*) FROM LocationHistory_new WHERE crewId = ?", arrayOf(crewId.toString()))
                    if (countCursor.moveToFirst()) {
                        val totalRecords = countCursor.getInt(0)
                        val recordsToUpdate = totalRecords / 2

                        // "하행"으로 업데이트할 기록들을 새로운 status로 삽입
                        db.execSQL("""
                    UPDATE LocationHistory_new
                    SET status = '하행'
                    WHERE crewId = $crewId AND time IN (
                        SELECT time FROM (
                            SELECT time FROM LocationHistory_new
                            WHERE crewId = $crewId
                            ORDER BY time
                            LIMIT $recordsToUpdate
                        )
                    )
                """.trimIndent())
                    }
                    countCursor.close()
                }
                cursor.close()

                // Step 4: 기존 테이블 삭제
                db.execSQL("DROP TABLE LocationHistory")

                // Step 5: 새로운 테이블 이름 변경
                db.execSQL("ALTER TABLE LocationHistory_new RENAME TO LocationHistory")
            }
        }

        fun getDatabase(
            context: Context
        ): LocationHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationHistoryDatabase::class.java,
                    DATABASE_NAME
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}