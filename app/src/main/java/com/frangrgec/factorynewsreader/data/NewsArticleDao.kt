package com.frangrgec.factorynewsreader.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsArticleDao {


    @Query("SELECT * FROM news_articles")
    fun getNewsArticles(): Flow<List<NewsArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsArticle>)

    @Query("DELETE FROM news_articles")
    suspend fun deleteNews()
}