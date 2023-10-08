package com.okta.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.okta.githubuser.data.local.entity.FavUserEntity

@Dao
interface FavUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favUserEntity: FavUserEntity)

    @Delete
    suspend fun delete(favUserEntity: FavUserEntity)

    @Query("SELECT * from favUsers ORDER BY username ASC")
    fun getAllFavUsers(): LiveData<List<FavUserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favUsers WHERE username = :username)")
    suspend fun isFavUser(username: String): Boolean
}