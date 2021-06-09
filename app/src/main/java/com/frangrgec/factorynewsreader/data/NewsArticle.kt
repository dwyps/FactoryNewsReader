package com.frangrgec.factorynewsreader.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "news_articles")
data class NewsArticle(
    @SerializedName("author")
    val author: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    @PrimaryKey
    val url: String,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String?,
    @SerializedName("publishedAt")
    val publishedAt: String?,
    @SerializedName("updateAt")
    val updateAt: Long = System.currentTimeMillis()
) : Serializable