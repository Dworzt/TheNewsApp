package com.example.thenewsapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thenewsapp.R
import com.example.thenewsapp.databinding.FragmentSearchBinding
import com.example.thenewsapp.adapters.NewsAdapter
import com.example.thenewsapp.ui.ArticleActivity
import com.example.thenewsapp.ui.NewsActivity
import com.example.thenewsapp.ui.NewsViewModel
import com.example.thenewsapp.util.Constants
import com.example.thenewsapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSearchBinding

    private var isError: Boolean = false
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var isScrolling: Boolean = false
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel

        // Show empty state initially
        showEmptyState()

        setupSearchRecycler()
        setupSearchEditText()
        setupObserver()
        setupRetryButton()
        setupSettingsButton()
    }

    private fun setupSettingsButton() {
        binding.btnSettings.setOnClickListener {
            val intent = Intent(activity, com.example.thenewsapp.ui.SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showEmptyState() {
        try {
            binding.emptyStateLayout?.visibility = View.VISIBLE
            binding.recyclerSearch.visibility = View.GONE
        } catch (e: Exception) {
            Log.e("SearchFragment", "Error showing empty state: ${e.message}")
        }
    }

    private fun hideEmptyState() {
        try {
            binding.emptyStateLayout?.visibility = View.GONE
            binding.recyclerSearch.visibility = View.VISIBLE
        } catch (e: Exception) {
            Log.e("SearchFragment", "Error hiding empty state: ${e.message}")
        }
    }

    private fun setupSearchEditText() {
        // For TextInputEditText, we can still use binding.searchEdit
        binding.searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                job?.cancel()
                job = MainScope().launch {
                    delay(Constants.SEARCH_NEWS_TIME_DELAY)
                    s?.let {
                        if (it.toString().isNotEmpty()) {
                            hideEmptyState()
                            hideErrorMessage()
                            newsViewModel.searchNews(it.toString())
                        } else {
                            showEmptyState()
                            // Clear previous results
                            newsAdapter.differ.submitList(emptyList())
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupObserver() {
        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    hideEmptyState()
                    response.data?.let { newsResponse ->
                        if (newsResponse.articles.isNotEmpty()) {
                            newsAdapter.differ.submitList(newsResponse.articles.toList())
                            val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                            isLastPage = newsViewModel.searchNewsPage == totalPages
                            if (isLastPage) {
                                binding.recyclerSearch.setPadding(0, 0, 0, 0)
                            }
                        } else {
                            // Show no results message
                            showNoResultsMessage()
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    hideEmptyState()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Sorry error: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    hideEmptyState()
                    showProgressBar()
                }
            }
        })
    }

    private fun showNoResultsMessage() {
        try {
            binding.recyclerSearch.visibility = View.GONE
            // You can create a no results layout or use empty state with different text
            showEmptyState()
        } catch (e: Exception) {
            Log.e("SearchFragment", "Error showing no results: ${e.message}")
        }
    }

    private fun setupSearchRecycler() {
        newsAdapter = NewsAdapter()
        binding.recyclerSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }

        newsAdapter.setOnItemClickListener { article ->
            try {
                val intent = Intent(activity, ArticleActivity::class.java).apply {
                    putExtra("article", article)
                }
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("SearchFragment", "Navigation failed: ${e.message}")
                Toast.makeText(context, "Cannot open article", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRetryButton() {
        try {
            val errorView = view?.findViewById<View>(R.id.itemSearchError)
            val retryButton = errorView?.findViewById<Button>(R.id.retryButton)

            retryButton?.setOnClickListener {
                val query = binding.searchEdit.text?.toString() ?: ""
                if (query.isNotEmpty()) {
                    hideErrorMessage()
                    newsViewModel.searchNews(query)
                } else {
                    Toast.makeText(context, "Please enter search term", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("SearchFragment", "Retry button setup failed: ${e.message}")
        }
    }

    private val scrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = (firstVisibleItemPosition + visibleItemCount) >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                val query = binding.searchEdit.text?.toString() ?: ""
                if (query.isNotEmpty()) {
                    newsViewModel.searchNews(query)
                }
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        try {
            val errorView = view?.findViewById<View>(R.id.itemSearchError)
            errorView?.visibility = View.INVISIBLE
            isError = false
        } catch (e: Exception) {
            Log.e("SearchFragment", "Error hiding error message: ${e.message}")
        }
    }

    private fun showErrorMessage(message: String) {
        try {
            val errorView = view?.findViewById<View>(R.id.itemSearchError)
            errorView?.visibility = View.VISIBLE

            val errorTextView = errorView?.findViewById<TextView>(R.id.errorText)
            errorTextView?.text = message
            isError = true
        } catch (e: Exception) {
            Log.e("SearchFragment", "Error showing error message: ${e.message}")
            Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }
}