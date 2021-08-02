package com.kt.recycleapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Database")
class MyRoomDatabase {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var no : Int = 0

    @ColumnInfo
    var barcode : String? = ""

    @ColumnInfo
    var dateTime: String? = ""

    @ColumnInfo
    var image: String? = ""

    @ColumnInfo
    var favorite: String? = ""

    constructor(barcode: String?, dateTime: String?,image:String?,favorite:String?){
        this.barcode = barcode
        this.dateTime = dateTime
        this.image = image
        this.favorite = favorite
    }
}