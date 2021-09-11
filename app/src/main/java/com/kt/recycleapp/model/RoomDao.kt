package com.kt.recycleapp.model

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface RoomDAO {
    @Query("select * from `Database`")
    fun getAll():List<MyRoomDatabase>

    @Insert(onConflict = REPLACE)
    fun insert(db:MyRoomDatabase)

    @Delete
    fun delete(db:MyRoomDatabase)

    @Query("UPDATE `Database` SET favorite = :state WHERE `no` == :position")
    fun updateFavorite(position:Int,state:String)

    @Query("SELECT `no` FROM `Database`")
    fun getNo():List<Long?>

    @Query("SELECT `barcode` FROM `Database` WHERE `no` == :position")
    fun getBarcode(position: Int):String

    @Query("select * from `Database` WHERE favorite = 'true'")
    fun getFavoriteAll() : List<MyRoomDatabase>

    @Query("DELETE FROM `Database`")
    fun deteteAll()
}