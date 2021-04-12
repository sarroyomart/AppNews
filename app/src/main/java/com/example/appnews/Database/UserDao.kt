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

    @Query("UPDATE UserModel SET fingerprint=:cid WHERE email=:eid")
    suspend fun updateFingerprint(cid:Int, eid: String)

    @Query("SELECT * FROM UserModel")
    suspend fun getAll():List<UserModel>

    @Query("SELECT * FROM UserModel WHERE email = :eid ")
    suspend fun  getUserId(eid: String): List<UserModel>

    @Query("DELETE FROM UserModel")
    suspend fun  nukeTable()

    @Query("UPDATE UserModel SET country=:cid WHERE email=:eid")
    suspend fun updateCountry(cid:String, eid: String)

    @Query("UPDATE UserModel SET language=:lid WHERE email=:eid")
    suspend fun updateLanguage(lid:String, eid: String)

    @Query("SELECT country FROM UserModel WHERE email = :eid")
    suspend fun getCountry(eid: String): String

    @Query("SELECT language FROM UserModel WHERE email = :eid")
    suspend fun getLanguage(eid: String): String

    @Query("SELECT userId FROM UserModel WHERE email = :eid")
    suspend fun getId(eid: String): String

    @Query("SELECT IV FROM UserModel WHERE email = :eid")
    suspend fun getKey(eid: String): ByteArray




}