package com.arya.submission2.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.submission2.data.local.datastore.SettingPreferences
import com.arya.submission2.data.remote.response.Item
import com.arya.submission2.databinding.ActivityFavoriteBinding
import com.arya.submission2.ui.adapter.UserAdapter
import com.arya.submission2.ui.viewmodel.FavoriteUserViewModel
import com.arya.submission2.ui.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val favoriteUserViewModel by viewModels<FavoriteUserViewModel> {
        ViewModelFactory.getInstance(application, SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.rvUser.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        favoriteUserViewModel.getAllFavoriteUsers().observe(this) {
            val items = arrayListOf<Item>()
            it.map {
                val item = Item(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            showUsers(items)
        }
    }

    private fun showUsers(users: ArrayList<Item>) {
        showTextEmpty(users.isEmpty())
        binding.rvUser.adapter = UserAdapter(users) {
            startActivity(
                Intent(
                    this@FavoriteActivity,
                    DetailActivity::class.java
                ).putExtra("EXTRA_USER", it.login)
            )
        }
    }

    private fun showTextEmpty(isEmpty: Boolean) {
        binding.textEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}