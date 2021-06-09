package com.frangrgec.factorynewsreader.shared.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.frangrgec.factorynewsreader.data.NewsArticle
import com.frangrgec.factorynewsreader.databinding.DetailedNewsViewBinding
import com.frangrgec.factorynewsreader.shared.recyclerview.NewsArticleComparator

class ViewPagerAdapter : ListAdapter<NewsArticle, ViewPagerViewHolder>(NewsArticleComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

        val binding = DetailedNewsViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}