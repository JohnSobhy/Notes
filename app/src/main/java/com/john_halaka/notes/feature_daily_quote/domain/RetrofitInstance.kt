package com.john_halaka.notes.feature_daily_quote.domain

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.api-ninjas.com"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("X-Api-Key", "9YuGN5OF9He9kjTQcSebKA==18xqjkGeUBaabkFX")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
    }

    val quoteService: QuoteService by lazy {
        retrofit.create(QuoteService::class.java)
    }

}