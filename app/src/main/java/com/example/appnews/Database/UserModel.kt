package com.example.appnews.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel (
        @PrimaryKey val email: String,

        @ColumnInfo(name = "userId")
        val userId: String?,
        @ColumnInfo(name = "fingerprint")
        val fingerprint:Int,
        @ColumnInfo(name= "country")
        val country:String,
        @ColumnInfo(name= "language")
        val language:String,
        @ColumnInfo(name = "IV")
        val iv:ByteArray
)