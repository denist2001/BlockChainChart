package com.codingchallenge.blockchainchart.di

import com.codingchallenge.blockchainchart.data.BlockChainRepositoryImpl
import com.codingchallenge.blockchainchart.data.RepositoryService
import com.codingchallenge.blockchainchart.domain.BlockChainRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkMockModule {

    @Provides
    @Singleton
    fun getUrl(): String = "http://localhost:8080/"

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, url: String) = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideRepositoryService(retrofit: Retrofit) =
        retrofit.create(RepositoryService::class.java)

    @Provides
    @Singleton
    fun provideRepository(repository: BlockChainRepositoryImpl): BlockChainRepository = repository
}