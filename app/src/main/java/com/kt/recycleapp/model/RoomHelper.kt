package com.kt.recycleapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(MyRoomDatabase::class),version = 2,exportSchema = false)
abstract class RoomHelper : RoomDatabase(){
    abstract fun databaseDao() :RoomDAO

    companion object {
        private var instance :RoomHelper? = null

        fun getInstance(context: Context):RoomHelper {
            val dbInstance = Room.databaseBuilder(context.applicationContext,RoomHelper::class.java,"Database").allowMainThreadQueries()
                .addMigrations(MIGRATION1_2).build()
            instance = dbInstance
            return dbInstance
        }

        private val MIGRATION1_2 = object :Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'Database' ADD COLUMN 'index'  INTEGER NOT NULL DEFAULT 0")
            }
        }
    }


}