package com.example.thenewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thenewsapp.R
import com.example.thenewsapp.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    var differ: AsyncListDiffer<Article> = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        val articleImage = holder.itemView.findViewById<ImageView>(R.id.articleImage)
        val articleTitle = holder.itemView.findViewById<TextView>(R.id.articleTitle)
        val articleSource = holder.itemView.findViewById<TextView>(R.id.articleSource)
        val articleDescription = holder.itemView.findViewById<TextView>(R.id.articleDescription)
        val articleDateTime = holder.itemView.findViewById<TextView>(R.id.articleDateTime)

        holder.itemView.apply {
            // Safe loading image dengan null check
            if (!article.urlToImage.isNullOrEmpty()) {
                Glide.with(this).load(article.urlToImage).into(articleImage)
            } else {
                articleImage.setImageResource(R.drawable.ic_launcher_background) // placeholder
            }

            // Safe access untuk source dengan null check
            articleSource.text = article.source?.name ?: "Unknown Source"
            articleTitle.text = article.title ?: "No Title"
            articleDescription.text = article.description ?: "No Description"
            articleDateTime.text = article.publishedAt ?: ""

            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}