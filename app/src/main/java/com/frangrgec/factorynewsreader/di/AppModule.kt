package com.frangrgec.factorynewsreader.di

import android.app.Application
import androidx.room.Room
import com.frangrgec.factorynewsreader.api.NewsAPI
import com.frangrgec.factorynewsreader.data.NewsArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(NewsAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsAPI =
        retrofit.create(NewsAPI::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): NewsArticleDatabase =
        Room.databaseBuilder(app, NewsArticleDatabase::class.java, "news_article_database")
            .fallbackToDestructiveMigration()
            .build()
}