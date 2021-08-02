package com.kt.recycleapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RoomDAO {
    @Query("select * from `Database`")
    fun getAll():List<MyRoomDatabase>

   /* @Query("select barcode from `Database`")
    fun getBarcode():String

    @Query("select dateTime from `Database`")
    fun getDateTime():String

    @Query("select image from `Database`")
    fun getImage():String*/

    @Insert(onConflict = REPLACE)
    fun insert(db:MyRoomDatabase)

    @Query("delete from `Database` where image = 'Recycle' ")
    fun delelte0()

    @Delete
    fun delete(db:MyRoomDatabase)
}