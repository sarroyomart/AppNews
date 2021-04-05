package com.example.appnews.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insertAllData(UserModel: UserModel)

    @Query("SELECT fingerprint FROM UserModel WHERE email = :eid")
    suspend fun getFingerprintByEmail(eid: String): Int

    @Query("SELECT * FROM UserModel")
    suspend fun getAll():List<UserModel>
}