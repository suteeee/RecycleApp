package com.kt.recycleapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RoomDAO {
    @Query("select * from `Database`")
    fun getAll():List<RoomDatabase>

    @Insert(onConflict = REPLACE)
    fun insert(db:RoomDatabase)

    @Delete
    fun delete(db:RoomDatabase)
}