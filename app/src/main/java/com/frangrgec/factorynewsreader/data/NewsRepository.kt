package com.frangrgec.factorynewsreader.data

import androidx.room.withTransaction
import com.frangrgec.factorynewsreader.api.NewsAPI
import com.frangrgec.factorynewsreader.util.Resource
import com.frangrgec.factorynewsreader.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsAPI,
    private val newsArticleDb: NewsArticleDatabase
) {

    private val newsArticleDao = newsArticleDb.newsArticleDao()

    fun getNews(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<NewsArticle>>> = networkBoundResource(
        query = {
            newsArticleDao.getNewsArticles()
        },
        fetch = {
            val response = newsApi.searchBBCNews()
            response.articles
        },
        saveFetchResult = { serverNewsArticles ->

            val newsArticles = serverNewsArticles.map { serverNewsArticle ->

                NewsArticle(
                    author = serverNewsArticle.author,
                    title = serverNewsArticle.title,
                    description = serverNewsArticle.description,
                    url = serverNewsArticle.url,
                    thumbnailUrl = serverNewsArticle.urlToImage,
                    publishedAt = serverNewsArticle.publishedAt
                )
            }

            newsArticleDb.withTransaction {
                newsArticleDao.deleteNews()
                newsArticleDao.insertArticles(newsArticles)
            }
        },
        shouldFetch = { cachedArticles ->

            if (forceRefresh) {
                true
            } else {

                val sortedArticles = cachedArticles.sortedBy { article ->
                    article.updateAt
                }

                val oldestTimestamp = sortedArticles.firstOrNull()?.updateAt
                val needsRefresh = oldestTimestamp == null ||
                        oldestTimestamp < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5)
                needsRefresh
            }
        },
        onFetchSuccess = onFetchSuccess,
        onFetchFailed = { t ->

            if (t !is HttpException && t !is IOException) {
                throw t
            }
            onFetchFailed(t)
        }
    )

    suspend fun updateArticle(article: NewsArticle) {
        newsArticleDao.updateArticle(article)
    }
}