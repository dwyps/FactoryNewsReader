package com.frangrgec.factorynewsreader.api


import com.frangrgec.factorynewsreader.BuildConfig.NEWS_API_ACCESS_KEY
import retrofit2.http.GET
import retrofit2.http.Headers

interface NewsAPI {

    companion object {
        const val BASE_URL = "https://newsapi.org/v1/"
        const val API_KEY = NEWS_API_ACCESS_KEY
    }

    //Could have used query but not needed, since only one static parameter
    @Headers("X-Api-Key: $API_KEY")
    @GET("articles?source=bbc-news&sortBy=top")
    suspend fun searchBBCNews(): NewsResponse
}