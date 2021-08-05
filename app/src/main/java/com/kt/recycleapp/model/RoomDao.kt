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

    @Query("UPDATE `database` SET favorite = :state WHERE `no` == :position")
    fun updateFavorite(position:Int,state:String)

    @Query("SELECT `no` FROM `database`")
    fun getNo():List<Long?>

    @Query("SELECT `barcode` FROM `database` WHERE `no` == :position")
    fun getBarcode(position: Int):String

    @Query("select * from `database` WHERE favorite = 'true'")
    fun getFavoriteAll() : List<MyRoomDatabase>
}