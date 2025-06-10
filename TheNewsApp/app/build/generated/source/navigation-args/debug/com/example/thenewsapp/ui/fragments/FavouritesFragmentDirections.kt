package com.example.thenewsapp.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import com.example.newsprojectpractice.R
import com.example.thenewsapp.models.Article
import java.io.Serializable
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.Suppress

public class FavouritesFragmentDirections private constructor() {
  private data class ActionFavouritesFragment2ToArticleFragment(
    public val article: Article,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_favouritesFragment2_to_articleFragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        if (Parcelable::class.java.isAssignableFrom(Article::class.java)) {
          result.putParcelable("article", this.article as Parcelable)
        } else if (Serializable::class.java.isAssignableFrom(Article::class.java)) {
          result.putSerializable("article", this.article as Serializable)
        } else {
          throw UnsupportedOperationException(Article::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        return result
      }
  }

  public companion object {
    public fun actionFavouritesFragment2ToArticleFragment(article: Article): NavDirections =
        ActionFavouritesFragment2ToArticleFragment(article)
  }
}
