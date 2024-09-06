package com.example.quote.di

import com.example.quote.retrofit.QuoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


const val url = "https://api.api-ninjas.com/v1/"

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(url).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun okkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun interceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val authRequest = request.newBuilder()
                .addHeader("X-Api-Key", "m2SmzcOEDu6vN0KoqbrSBQ==Cla6KOOEjQIPODiA").build()
            chain.proceed(authRequest)
        }
    }

    @Provides
    @Singleton
    fun quoteApi(retrofit: Retrofit):QuoteDao = retrofit.create(QuoteDao::class.java)
}