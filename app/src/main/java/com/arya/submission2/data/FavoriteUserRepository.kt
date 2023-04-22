package com.arya.submission2.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.arya.submission2.data.local.entity.FavoriteUser
import com.arya.submission2.data.local.room.FavoriteUserDao
import com.arya.submission2.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(private val application: Application) {
    private val favoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        favoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = favoriteUserDao.getAllFavoriteUser()
    fun getFavoriteUsers(username: String): LiveData<FavoriteUser> =
        favoriteUserDao.getFavoriteUserByUsername(username)

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { favoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { favoriteUserDao.delete(favoriteUser) }
    }
}