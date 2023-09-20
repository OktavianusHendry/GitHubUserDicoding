package com.okta.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.okta.githubuser.data.response.GitHubResponse
import com.okta.githubuser.data.response.ItemsItem
import com.okta.githubuser.retrofit.ApiConfig
import com.okta.githubuser.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        findUser()
    }

    fun findUser(username: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(query = username)
        client.enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.value = response.body()?.items as List<ItemsItem>
                        val listUserSize = _listUser.value?.size ?: 0
                        val snackbarTextMessage = if (listUserSize == 0) {
                            "GitHub User not Found"
                        } else {
                            "Found $listUserSize"
                        }
                        _snackbarText.value = Event(snackbarTextMessage)
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}