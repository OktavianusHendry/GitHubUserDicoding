package com.okta.githubuser.di

import android.content.Context
import com.okta.githubuser.data.FavUsersRepository
import com.okta.githubuser.data.local.room.FavUserDatabase
import com.okta.githubuser.data.remote.retrofit.ApiConfig
import com.okta.githubuser.util.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavUsersRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavUserDatabase.getInstance(context)
        val dao = database.favUserDao()
        val appExecutors = AppExecutors()
        return FavUsersRepository.getInstance(apiService, dao, appExecutors)
    }
}