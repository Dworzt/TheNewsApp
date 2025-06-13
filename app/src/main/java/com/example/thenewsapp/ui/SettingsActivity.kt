package com.example.thenewsapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.thenewsapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val THEME_PREFS = "theme_prefs"
        const val IS_DARK_MODE = "is_dark_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupSharedPreferences()
        setupThemeToggle()
        setupAppInfo()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Settings"
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupSharedPreferences() {
        sharedPreferences = getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean(IS_DARK_MODE, false)
        binding.switchDarkMode.isChecked = isDarkMode
    }

    private fun setupThemeToggle() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            saveThemePreference(isChecked)
            applyTheme(isChecked)
        }

        binding.themeLayout.setOnClickListener {
            binding.switchDarkMode.toggle()
        }
    }

    private fun setupAppInfo() {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            binding.versionText.text = "Version ${packageInfo.versionName}"
        } catch (e: Exception) {
            binding.versionText.text = "Version 1.0.0"
        }
    }

    private fun saveThemePreference(isDarkMode: Boolean) {
        sharedPreferences.edit()
            .putBoolean(IS_DARK_MODE, isDarkMode)
            .apply()
    }

    private fun applyTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun sendFeedback() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("developer@thenewsapp.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback for The News App")
            putExtra(Intent.EXTRA_TEXT, "Hi, I would like to share my feedback about The News App:\n\n")
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Feedback"))
        } catch (e: Exception) {
            // Handle case where no email app is available
        }
    }

    private fun rateApp() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            startActivity(intent)
        }
    }

    private fun openPrivacyPolicy() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://thenewsapp.com/privacy"))
        startActivity(intent)
    }
}