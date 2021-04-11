package com.example.appnews.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserModel::class], version =4, exportSchema = false)
abstract class DatabaseClass: RoomDatabase() {
    abstract fun getUserDao(): UserDao

    companion object{
        @Volatile
        private var instance: DatabaseClass? = null

        fun getDatabase(context: Context): DatabaseClass{
            return instance ?: synchronized(this) {
                val instancex = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseClass::class.java,
                    "appnews"
                ).fallbackToDestructiveMigration().build()
                instance = instancex
                // return instance
                instancex
            }

        }

    }

}