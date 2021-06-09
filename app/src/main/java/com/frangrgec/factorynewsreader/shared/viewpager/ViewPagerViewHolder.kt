package com.frangrgec.factorynewsreader.shared.viewpager

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frangrgec.factorynewsreader.R
import com.frangrgec.factorynewsreader.data.NewsArticle
import com.frangrgec.factorynewsreader.databinding.DetailedNewsViewBinding

class ViewPagerViewHolder(
    private val binding: DetailedNewsViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: NewsArticle) {

        binding.apply {
            Glide.with(itemView)
                .load(article.thumbnailUrl)
                .error(R.drawable.image_placeholder)
                .into(imageView)

            textViewTitle.text = article.title ?: ""
            textViewDescription.text = article.description ?: ""
        }
    }
}