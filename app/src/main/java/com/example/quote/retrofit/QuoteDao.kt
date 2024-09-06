package com.example.quote.retrofit

import com.example.quote.data.entity.QuotesRespond
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface QuoteDao {
    @GET("quotes")
    suspend fun getQuote():Response<QuotesRespond>
}