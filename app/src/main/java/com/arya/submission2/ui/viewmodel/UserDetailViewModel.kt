package com.arya.submission2.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.submission2.data.FavoriteUserRepository
import com.arya.submission2.data.local.entity.FavoriteUser
import com.arya.submission2.data.remote.retrofit.ApiConfig
import com.arya.submission2.data.remote.response.Item
import com.arya.submission2.data.remote.response.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    private val _isLoadingFollowers = MutableLiveData<Boolean>()
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _userFollowing = MutableLiveData<ArrayList<Item>>()
    val userFollowing: LiveData<ArrayList<Item>> = _userFollowing

    private val _userFollowers = MutableLiveData<ArrayList<Item>>()
    val userFollowers: LiveData<ArrayList<Item>> = _userFollowers

    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getFavoriteUser(username: String): LiveData<FavoriteUser> =
        favoriteUserRepository.getFavoriteUsers(username)

    fun insert(favoriteUser: FavoriteUser) {
        favoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        favoriteUserRepository.delete(favoriteUser)
    }


    fun loadUser(id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(id)
        client.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                    loadUserFollowing(id)
                    loadUserFollowers(id)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun loadUserFollowing(id: String) {
        _isLoadingFollowing.value = true
        val client = ApiConfig.getApiService().getUserFollowing(id)
        client.enqueue(object : Callback<ArrayList<Item>> {
            override fun onResponse(
                call: Call<ArrayList<Item>>,
                response: Response<ArrayList<Item>>
            ) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                _isLoadingFollowing.value = false
            }
        })
    }

    fun loadUserFollowers(id: String) {
        _isLoadingFollowers.value = true
        val client = ApiConfig.getApiService().getUserFollowers(id)
        client.enqueue(object : Callback<ArrayList<Item>> {
            override fun onResponse(
                call: Call<ArrayList<Item>>,
                response: Response<ArrayList<Item>>
            ) {
                _isLoadingFollowers.value = false
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                _isLoadingFollowers.value = false
            }
        })
    }

}