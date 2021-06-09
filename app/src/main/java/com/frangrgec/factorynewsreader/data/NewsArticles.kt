package com.frangrgec.factorynewsreader.data

import com.google.gson.annotations.SerializedName

data class NewsArticles(
    @SerializedName("newsArticles")
    val newsArticles: List<NewsArticle>
)