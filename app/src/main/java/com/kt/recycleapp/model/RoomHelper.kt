package com.kt.recycleapp.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MyRoomDatabase::class),version = 1,exportSchema = false)
abstract class RoomHelper : RoomDatabase(){
    abstract fun databaseDao() :RoomDAO
}