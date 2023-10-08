package com.okta.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.okta.githubuser.data.local.entity.FavUserEntity

@Database(entities = [FavUserEntity::class], version = 1, exportSchema = false)
abstract class FavUserDatabase : RoomDatabase() {
    abstract fun favUserDao(): FavUserDao

    companion object {
        @Volatile
        private var instance: FavUserDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): FavUserDatabase {
            if (instance == null) {
                synchronized(FavUserDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavUserDatabase::class.java, "GitHubUser.db"
                    )
                        .build()
                }
            }
            return instance as FavUserDatabase
        }
    }
}