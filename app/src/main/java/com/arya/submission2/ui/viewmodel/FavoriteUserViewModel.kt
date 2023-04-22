package com.arya.submission2.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arya.submission2.data.FavoriteUserRepository
import com.arya.submission2.data.local.entity.FavoriteUser

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = favoriteUserRepository.getAllFavoriteUsers()
}