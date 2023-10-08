package com.okta.githubuser.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.okta.githubuser.data.FavUsersRepository
import com.okta.githubuser.di.Injection

class FavUserViewModelFactory private constructor(private val favUsersRepository: FavUsersRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavUserViewModel::class.java)) {
            return FavUserViewModel(favUsersRepository) as T
        } else if(modelClass.isAssignableFrom(DetailUserViewModel::class.java)){
            return DetailUserViewModel(favUsersRepository) as T
        } else{
            throw Throwable("Unknown ViewModel class:" + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: FavUserViewModelFactory? = null
        fun getInstance(context: Context): FavUserViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FavUserViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
