package com.arya.submission2.ui.viewmodel

import androidx.lifecycle.*
import com.arya.submission2.data.local.datastore.SettingPreferences
import com.arya.submission2.data.remote.retrofit.ApiConfig
import com.arya.submission2.data.remote.response.GithubResponse
import com.arya.submission2.data.remote.response.Item
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _users = MutableLiveData<ArrayList<Item>>()
    val users: LiveData<ArrayList<Item>> = _users

    init {
        loadUsers("dicoding")
    }

    fun loadUsers(q: String) {
        _isLoading.value = true
        _users.value?.clear()
        val client = ApiConfig.getApiService().searchUser(q)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _users.value = response.body()?.items
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}