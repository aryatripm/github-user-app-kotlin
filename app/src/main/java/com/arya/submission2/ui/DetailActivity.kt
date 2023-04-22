package com.arya.submission2.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.arya.submission2.R
import com.arya.submission2.data.local.datastore.SettingPreferences
import com.arya.submission2.data.local.entity.FavoriteUser
import com.arya.submission2.data.remote.response.User
import com.arya.submission2.ui.adapter.FollowAdapter
import com.arya.submission2.databinding.ActivityDetailBinding
import com.arya.submission2.ui.viewmodel.FavoriteUserViewModel
import com.arya.submission2.ui.viewmodel.UserDetailViewModel
import com.arya.submission2.ui.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val userDetailViewModel by viewModels<UserDetailViewModel> {
        ViewModelFactory.getInstance(application, SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        val username = intent.getStringExtra("EXTRA_USER")
        var typeAction = ""
        var favoriteUser = FavoriteUser()
        var user = User()
        if (username != null) {
            userDetailViewModel.loadUser(username)
            userDetailViewModel.getFavoriteUser(username).observe(this) {
                if (it != null) {
                    favoriteUser = it
                    typeAction = "DELETE"
                    binding.buttonFavorite.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.ic_round_favorite_24
                        )
                    )
                } else {
                    typeAction = "INSERT"
                    binding.buttonFavorite.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.ic_baseline_favorite_border_24
                        )
                    )
                }
            }
        }
        userDetailViewModel.user.observe(this) {
            Glide.with(this).load(it.avatarUrl).into(binding.imageDetail)
            binding.nameDetail.text = it.name
            binding.typeDetail.text = it.login
            binding.followers.text = it.followers.toString()
            binding.following.text = it.following.toString()
            user = it
        }
        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.buttonFavorite.setOnClickListener { v ->
            if (typeAction == "DELETE") {
                userDetailViewModel.delete(favoriteUser)
                showSnackbar("Berhasil menghapus data.")
            } else {
                favoriteUser.username = username ?: ""
                favoriteUser.avatarUrl = user.avatarUrl
                favoriteUser.type = user.type
                userDetailViewModel.insert(favoriteUser)
                showSnackbar("Berhasil menambahkan data.")
            }
        }

        val sectionsPagerAdapter = FollowAdapter(this)
        sectionsPagerAdapter.username = username ?: ""
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(binding.root, message, duration).show()
    }
}