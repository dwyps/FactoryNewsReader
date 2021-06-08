package com.frangrgec.factorynewsreader.shared

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frangrgec.factorynewsreader.R
import com.frangrgec.factorynewsreader.data.NewsArticle
import com.frangrgec.factorynewsreader.databinding.ItemNewsArticleBinding

class NewsArticleViewHolder(
    private val binding: ItemNewsArticleBinding,
    private val onItemClick: (Int) -> Unit
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

    init {
        binding.apply {
            root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position)
                }
            }
        }
    }

}
