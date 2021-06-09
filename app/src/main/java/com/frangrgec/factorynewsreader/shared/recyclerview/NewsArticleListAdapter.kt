package com.frangrgec.factorynewsreader.shared.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.frangrgec.factorynewsreader.data.NewsArticle
import com.frangrgec.factorynewsreader.databinding.ItemNewsArticleBinding

class NewsArticleListAdapter (
    private val onItemClick: (NewsArticle, Int) -> Unit
) : ListAdapter<NewsArticle, NewsArticleViewHolder>(NewsArticleComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {

        val binding =
            ItemNewsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsArticleViewHolder(binding,
            onItemClick = { position ->
                val article = getItem(position)
                if (article != null) {
                    onItemClick(article, position)
                }
            })
    }

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int) {

        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}