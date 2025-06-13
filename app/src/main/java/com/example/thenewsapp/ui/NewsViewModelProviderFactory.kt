package com.example.thenewsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.thenewsapp.repository.NewsRepository
import androidx.lifecycle.ViewModelProvider

class NewsViewModelProviderFactory(val app:Application, val newsRepository: NewsRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}