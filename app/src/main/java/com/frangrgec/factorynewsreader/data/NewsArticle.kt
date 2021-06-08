package com.frangrgec.factorynewsreader.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "news_articles")
data class NewsArticle(
    val author: String?,
    val title: String?,
    val description: String?,
    @PrimaryKey val url: String,
    val thumbnailUrl: String?,
    val publishedAt: String?,
    val updateAt: Long = System.currentTimeMillis()
)