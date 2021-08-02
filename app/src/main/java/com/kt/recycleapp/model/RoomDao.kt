package com.kt.recycleapp.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface RoomDAO {
    @Query("select * from `database`")
    fun getAll():List<MyRoomDatabase>

    @Insert(onConflict = REPLACE)
    fun insert(db:MyRoomDatabase)

    @Delete
    fun delete(db:MyRoomDatabase)

    @Query("UPDATE `database` SET favorite = 'true' WHERE `no` == :position")
    fun updateFavorite(position:Int)

    @Query("SELECT `no` FROM `database`")
    fun getNo():List<Long?>
}