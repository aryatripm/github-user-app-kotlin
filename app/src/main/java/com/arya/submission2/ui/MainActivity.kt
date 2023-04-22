package com.arya.submission2.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.get
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.submission2.R
import com.arya.submission2.data.local.datastore.SettingPreferences
import com.arya.submission2.ui.adapter.UserAdapter
import com.arya.submission2.databinding.ActivityMainBinding
import com.arya.submission2.data.remote.response.Item
import com.arya.submission2.ui.viewmodel.UserDetailViewModel
import com.arya.submission2.ui.viewmodel.UserViewModel
import com.arya.submission2.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val userViewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(application, SettingPreferences.getInstance(dataStore))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        userViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.mainToolbar.menu[0].setOnMenuItemClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    SettingsActivity::class.java
                )
            )
            return@setOnMenuItemClickListener true
        }

        binding.rvUser.layoutManager = LinearLayoutManager(this)

        userViewModel.users.observe(this) {
            showUsers(it)
        }
        userViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.mainSearch.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    userViewModel.loadUsers(query)
                }
                binding.mainSearch.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        binding.fab.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    FavoriteActivity::class.java
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        SettingsActivity::class.java
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showUsers(users: ArrayList<Item>) {
        binding.rvUser.adapter = UserAdapter(users) {
            startActivity(
                Intent(
                    this@MainActivity,
                    DetailActivity::class.java
                ).putExtra("EXTRA_USER", it.login)
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}