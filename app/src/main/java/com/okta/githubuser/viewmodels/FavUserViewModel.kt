package com.okta.githubuser.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.okta.githubuser.data.FavUsersRepository
import com.okta.githubuser.data.local.entity.FavUserEntity

class FavUserViewModel(private val favUsersRepository: FavUsersRepository) : ViewModel() {
    fun getFavUsers(): LiveData<List<FavUserEntity>> = favUsersRepository.getFavUsers()
}