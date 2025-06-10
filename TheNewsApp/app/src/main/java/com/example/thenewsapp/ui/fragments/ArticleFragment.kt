package com.example.thenewsapp.ui.fragments

import com.example.newsprojectpractice.R
import com.example.newsprojectpractice.databinding.FragmentArticleBinding
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.thenewsapp.ui.NewsViewModel
import androidx.navigation.fragment.navArgs
import android.webkit.WebViewClient
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var newsViewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        newsViewModel = (activity as com.example.thenewsapp.ui.NewsActivity).newsViewModel
        val article = args.article

        binding.webView.apply {
            var webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }

        binding.fab.setOnClickListener {
            newsViewModel.addToFavourites(article)
            Snackbar.make(view, "Added to favourites", Snackbar.LENGTH_SHORT).show()
        }
    }
}


