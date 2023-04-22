package com.arya.submission2.ui

import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.arya.submission2.data.local.datastore.SettingPreferences
import com.arya.submission2.databinding.SettingsActivityBinding
import com.arya.submission2.ui.viewmodel.UserViewModel
import com.arya.submission2.ui.viewmodel.ViewModelFactory

class SettingsActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val userViewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(application, SettingPreferences.getInstance(dataStore))
    }
    private lateinit var binding: SettingsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        userViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            userViewModel.saveThemeSetting(isChecked)
        }

    }
}