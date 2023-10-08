package com.okta.githubuser.data

import com.okta.githubuser.data.local.entity.FavUserEntity
import com.okta.githubuser.data.local.room.FavUserDao
import com.okta.githubuser.data.remote.retrofit.ApiService
import com.okta.githubuser.util.AppExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavUsersRepository private constructor(
    private val apiService: ApiService,
    private val favUserDao: FavUserDao,
    private val appExecutors: AppExecutors
) {

    fun getFavUsers() = favUserDao.getAllFavUsers()

    suspend fun isFavUser(username: String): Boolean =
        withContext(Dispatchers.IO) { favUserDao.isFavUser(username) }

    suspend fun insert(favUser: FavUserEntity) =
        withContext(Dispatchers.IO) { favUserDao.insert(favUser) }

    suspend fun delete(favUser: FavUserEntity) =
        withContext(Dispatchers.IO) { favUserDao.delete(favUser) }

    companion object {
        @Volatile
        private var instance: FavUsersRepository? = null
        fun getInstance(
            apiService: ApiService,
            favUserDao: FavUserDao,
            appExecutors: AppExecutors
        ): FavUsersRepository = instance ?: synchronized(this) {
            instance ?: FavUsersRepository(apiService, favUserDao, appExecutors)
        }.also { instance = it }
    }
}