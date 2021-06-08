package com.frangrgec.factorynewsreader.api

data class NewsArticleDto(
    val author: String?,
    val title: String?,
    val url: String,
    val urlToImage: String?,
    val description: String?,
    val publishedAt: String?
)