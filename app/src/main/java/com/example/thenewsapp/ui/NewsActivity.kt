package com.example.thenewsapp.ui

        import android.os.Bundle
        import androidx.appcompat.app.AppCompatActivity
        import androidx.lifecycle.ViewModelProvider
        import androidx.navigation.fragment.NavHostFragment
        import androidx.navigation.ui.setupWithNavController
        import com.example.thenewsapp.databinding.ActivityNewsBinding
        import com.example.thenewsapp.R
        import com.example.thenewsapp.db.ArticleDatabase
        import com.example.thenewsapp.repository.NewsRepository

        class NewsActivity : AppCompatActivity() {

            lateinit var newsViewModel: NewsViewModel
            lateinit var binding: ActivityNewsBinding

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = ActivityNewsBinding.inflate(layoutInflater)
                setContentView(binding.root)

                val newsRepository = NewsRepository(ArticleDatabase(this))
                val newsViewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
                newsViewModel = ViewModelProvider(this, newsViewModelProviderFactory).get(NewsViewModel::class.java)

                val newsHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = newsHostFragment.navController
                binding.bottomNavigationView.setupWithNavController(navController)
            }
        }