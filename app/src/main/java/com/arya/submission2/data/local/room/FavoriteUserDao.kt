package com.arya.submission2.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.arya.submission2.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * from favorite_user")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM favorite_user WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

}