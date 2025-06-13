package com.example.thenewsapp.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.thenewsapp.databinding.ActivityArticleBinding
import com.example.thenewsapp.models.Article
import com.example.thenewsapp.repository.NewsRepository
import com.example.thenewsapp.db.ArticleDatabase
import com.google.android.material.snackbar.Snackbar

class ArticleActivity : AppCompatActivity() {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ViewModel
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        newsViewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        // Get article from intent
        val article = intent.getParcelableExtra<Article>("article")

        article?.let { currentArticle ->
            setupWebView(currentArticle)
            setupFab(currentArticle)
        }
    }

    private fun setupWebView(article: Article) {
        // Show progress bar
        binding.apply {
            // Assuming you have a progress bar in layout
            webView.visibility = View.GONE

            webView.apply {
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        // Show loading
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // Hide loading and show webview
                        webView.visibility = View.VISIBLE
                    }

                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        return false // Let WebView handle the URL
                    }
                }

                // Optimize WebView settings
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    builtInZoomControls = true
                    displayZoomControls = false
                    setSupportZoom(true)
                    defaultTextEncodingName = "utf-8"

                    // Disable cache for fresh content
                    cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
                }

                // Load URL
                article.url?.let { url ->
                    loadUrl(url)
                } ?: run {
                    loadData("<h1>Article URL not available</h1>", "text/html", "UTF-8")
                }
            }
        }
    }

    private fun setupFab(article: Article) {
        binding.fab.setOnClickListener { view ->
            try {
                newsViewModel.addToFavourites(article)
                Snackbar.make(view, "Added to favourites", Snackbar.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Snackbar.make(view, "Failed to add to favourites", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webView.destroy()
    }
}