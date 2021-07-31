package com.kt.recycleapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Database")
class RoomDatabase {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var no : Long? = null

    @ColumnInfo
    var barcode : String? = ""

    @ColumnInfo
    var dateTime: String? = ""

    @ColumnInfo
    var image: String? = ""

    constructor(barcode: String?, dateTime: String?,image:String?){
        this.barcode = barcode
        this.dateTime = dateTime
        this.image = image
    }



}