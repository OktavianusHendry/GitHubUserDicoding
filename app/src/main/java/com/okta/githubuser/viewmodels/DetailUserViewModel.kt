package com.okta.githubuser.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okta.githubuser.data.FavUsersRepository
import com.okta.githubuser.data.local.entity.FavUserEntity
import com.okta.githubuser.data.remote.response.DetailUserResponse
import com.okta.githubuser.data.remote.response.ItemsItem
import com.okta.githubuser.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(
    private val favUsersRepository: FavUsersRepository
) : ViewModel() {

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _listUserFollower = MutableLiveData<List<ItemsItem>>()
    val listUserFollower: LiveData<List<ItemsItem>> = _listUserFollower

    private val _listUserFollowing = MutableLiveData<List<ItemsItem>>()
    val listUserFollowing: LiveData<List<ItemsItem>> = _listUserFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingFollower = MutableLiveData<Boolean>()
    val isLoadingFollower: LiveData<Boolean> = _isLoadingFollower

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    fun save(favUser: FavUserEntity) = viewModelScope.launch { favUsersRepository.insert(favUser) }

    fun delete(favUser: FavUserEntity) =
        viewModelScope.launch { favUsersRepository.delete(favUser) }

    fun isFavoriteUser(username: String) =
        viewModelScope.async { favUsersRepository.isFavUser(username) }

    fun findUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response}")
                }
            }

            override fun onFailure(
                call: Call<DetailUserResponse>,
                t: Throwable
            ) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findUserFollowing(username: String) {
        _isLoadingFollowing.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    _listUserFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findUserFollower(username: String) {
        _isLoadingFollower.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoadingFollower.value = false
                if (response.isSuccessful) {
                    _listUserFollower.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollower.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailUserViewModel"
    }
}